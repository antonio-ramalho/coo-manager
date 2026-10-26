import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Component } from '@angular/core';
import { Observable, of } from 'rxjs';
import { describe, expect, it, vi } from 'vitest';
import { SearchableSelectComponent } from './searchable-select.component';

interface MockItem {
  id: number;
  name: string;
  code: string;
}

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, SearchableSelectComponent],
  template: `
    <app-searchable-select
      [formControl]="control"
      [options]="items"
      labelKey="name"
      valueKey="id"
      placeholder="Pesquisar item..."
    ></app-searchable-select>
  `,
})
class TestHostComponent {
  control = new FormControl<number | null>(null);
  items: MockItem[] = [
    { id: 1, name: 'Abobrinha Italiana', code: 'ABO-01' },
    { id: 2, name: 'Adubo NPK 10-10-10', code: 'ADU-02' },
    { id: 3, name: 'Milho Verde', code: 'MIL-03' },
  ];
}

@Component({
  standalone: true,
  imports: [ReactiveFormsModule, SearchableSelectComponent],
  template: `
    <app-searchable-select
      [formControl]="control"
      [searchFn]="searchFn"
      labelKey="name"
      valueKey="id"
      placeholder="Pesquisa remota..."
    ></app-searchable-select>
  `,
})
class RemoteHostComponent {
  control = new FormControl<number | null>(null);
  searchFn = (term: string): Observable<MockItem[]> => {
    const all: MockItem[] = [
      { id: 1, name: 'Abobrinha Italiana', code: 'ABO-01' },
      { id: 2, name: 'Adubo NPK 10-10-10', code: 'ADU-02' },
    ];
    return of(all.filter((x) => x.name.toLowerCase().includes(term.toLowerCase())));
  };
}

describe('SearchableSelectComponent', () => {
  let fixture: ComponentFixture<TestHostComponent>;
  let host: TestHostComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TestHostComponent, RemoteHostComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(TestHostComponent);
    host = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create and render search placeholder', () => {
    const inputEl = fixture.nativeElement.querySelector('input');
    expect(inputEl).toBeTruthy();
    expect(inputEl.placeholder).toBe('Pesquisar item...');
  });

  it('should filter options by search term', () => {
    const selectComp: SearchableSelectComponent<MockItem> = fixture.debugElement.children[0].componentInstance;
    selectComp.searchControl.setValue('milho');
    fixture.detectChanges();

    const filtered = selectComp.filteredOptions;
    expect(filtered.length).toBe(1);
    expect(filtered[0].name).toBe('Milho Verde');
  });

  it('should select an item, update formControl value and display selected label', () => {
    const selectComp: SearchableSelectComponent<MockItem> = fixture.debugElement.children[0].componentInstance;
    selectComp.selectItem(host.items[1]);
    fixture.detectChanges();

    expect(host.control.value).toBe(2);
    expect(selectComp.selectedItem()?.name).toBe('Adubo NPK 10-10-10');

    // Estado selecionado exibido
    const labelSpan = fixture.nativeElement.querySelector('span');
    expect(labelSpan.textContent.trim()).toBe('Adubo NPK 10-10-10');
  });

  it('should clear selection when clearSelection is called', () => {
    const selectComp: SearchableSelectComponent<MockItem> = fixture.debugElement.children[0].componentInstance;
    selectComp.selectItem(host.items[0]);
    fixture.detectChanges();
    expect(host.control.value).toBe(1);

    selectComp.clearSelection();
    fixture.detectChanges();

    expect(host.control.value).toBeNull();
    expect(selectComp.selectedItem()).toBeNull();
  });

  it('should sync selected item when formControl value changes from outside (writeValue)', () => {
    host.control.setValue(3);
    fixture.detectChanges();

    const selectComp: SearchableSelectComponent<MockItem> = fixture.debugElement.children[0].componentInstance;
    expect(selectComp.selectedItem()?.id).toBe(3);
    expect(selectComp.selectedItem()?.name).toBe('Milho Verde');
  });

  it('should support remote search with searchFn on open and input change', () => {
    vi.useFakeTimers();
    try {
      const remoteFixture = TestBed.createComponent(RemoteHostComponent);
      remoteFixture.detectChanges();
      const selectComp: SearchableSelectComponent<MockItem> = remoteFixture.debugElement.children[0].componentInstance;

      // Abrir dropdown busca inicial vazia
      selectComp.open();
      remoteFixture.detectChanges();
      vi.advanceTimersByTime(50);

      expect(selectComp.displayedOptions.length).toBe(2);

      // Digitar termo com debounce
      selectComp.searchControl.setValue('abobrinha');
      vi.advanceTimersByTime(350);
      remoteFixture.detectChanges();

      expect(selectComp.displayedOptions.length).toBe(1);
      expect(selectComp.displayedOptions[0].name).toBe('Abobrinha Italiana');
    } finally {
      vi.useRealTimers();
    }
  });
});

