import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { StandardFormLayoutComponent } from './standard-form-layout.component';
import { BreakpointObserver } from '@angular/cdk/layout';
import { of } from 'rxjs';

describe('StandardFormLayoutComponent', () => {
  let component: StandardFormLayoutComponent;
  let fixture: ComponentFixture<StandardFormLayoutComponent>;

  const mockBreakpointObserver = {
    observe: vi.fn().mockReturnValue(of({ matches: true })),
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [StandardFormLayoutComponent],
      providers: [
        { provide: BreakpointObserver, useValue: mockBreakpointObserver },
      ],
    });

    fixture = TestBed.createComponent(StandardFormLayoutComponent);
    component = fixture.componentInstance;
    component.title = 'Cadastro de Teste';
  });

  it('deve inicializar com sucesso', () => {
    expect(component).toBeTruthy();
    expect(component.title).toBe('Cadastro de Teste');
  });

  it('deve emitir evento de cancelamento', () => {
    const cancelSpy = vi.fn();
    component.cancel.subscribe(cancelSpy);
    component.cancel.emit();
    expect(cancelSpy).toHaveBeenCalled();
  });
});
