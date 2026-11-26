import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { TransactionalFormLayoutComponent } from './transactional-form-layout.component';
import { BreakpointObserver } from '@angular/cdk/layout';
import { of } from 'rxjs';

describe('TransactionalFormLayoutComponent', () => {
  let component: TransactionalFormLayoutComponent;
  let fixture: ComponentFixture<TransactionalFormLayoutComponent>;

  const mockBreakpointObserver = {
    observe: vi.fn().mockReturnValue(of({ matches: true })),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TransactionalFormLayoutComponent],
      providers: [
        { provide: BreakpointObserver, useValue: mockBreakpointObserver },
      ],
    });

    fixture = TestBed.createComponent(TransactionalFormLayoutComponent);
    component = fixture.componentInstance;
    component.title = 'Nova Venda';
  });

  it('deve inicializar com sucesso', () => {
    expect(component).toBeTruthy();
    expect(component.title).toBe('Nova Venda');
  });

  it('deve emitir evento de cancelamento', () => {
    const cancelSpy = vi.fn();
    component.cancel.subscribe(cancelSpy);
    component.cancel.emit();
    expect(cancelSpy).toHaveBeenCalled();
  });
});
