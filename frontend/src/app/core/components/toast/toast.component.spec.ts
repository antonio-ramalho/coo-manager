import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, expect, it, beforeEach } from 'vitest';
import { ToastComponent } from './toast.component';
import { ToastService } from '../../services/toast.service';

describe('ToastComponent', () => {
  let component: ToastComponent;
  let fixture: ComponentFixture<ToastComponent>;
  let toastService: ToastService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToastComponent],
      providers: [ToastService],
    }).compileComponents();

    fixture = TestBed.createComponent(ToastComponent);
    component = fixture.componentInstance;
    toastService = TestBed.inject(ToastService);
    fixture.detectChanges();
  });

  it('deve deduplicar toasts com mesmo tipo e mensagem', () => {
    toastService.showError('Erro de conexão.');
    toastService.showError('Erro de conexão.');
    toastService.showError('Erro de conexão.');

    expect(component.toasts()).toHaveLength(1);
    expect(component.toasts()[0].message).toBe('Erro de conexão.');
  });

  it('deve ordenar toasts por prioridade (error > warning > success)', () => {
    toastService.showSuccess('Operação concluída.');
    toastService.showWarning('Campo inválido.');
    toastService.showError('Falha crítica.');

    const list = component.toasts();
    expect(list).toHaveLength(3);
    expect(list[0].type).toBe('error');
    expect(list[1].type).toBe('warning');
    expect(list[2].type).toBe('success');
  });

  it('deve limitar a exibição simultânea ao máximo de 3 toasts', () => {
    toastService.showSuccess('Sucesso 1');
    toastService.showSuccess('Sucesso 2');
    toastService.showWarning('Aviso 1');
    toastService.showError('Erro 1');

    const list = component.toasts();
    expect(list).toHaveLength(3);
    expect(list[0].type).toBe('error');
    expect(list[1].type).toBe('warning');
  });
});

