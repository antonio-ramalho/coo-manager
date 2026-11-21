import { ComponentFixture, TestBed } from '@angular/core/testing';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { ActionMenuComponent } from './action-menu.component';
import { AuthService } from '../../../../core/services/auth.service';
import { ActionMenuItem } from './action-menu-item.model';
import { Permission } from '../../../enums/permission.enum';

describe('ActionMenuComponent', () => {
  let component: ActionMenuComponent;
  let fixture: ComponentFixture<ActionMenuComponent>;
  let authServiceMock: { hasPermission: ReturnType<typeof vi.fn> };

  beforeEach(() => {
    authServiceMock = {
      hasPermission: vi.fn().mockReturnValue(true),
    };

    TestBed.configureTestingModule({
      imports: [ActionMenuComponent],
      providers: [{ provide: AuthService, useValue: authServiceMock }],
    });

    fixture = TestBed.createComponent(ActionMenuComponent);
    component = fixture.componentInstance;
  });

  it('deve inicializar fechado por padrão', () => {
    expect(component.isOpen()).toBe(false);
  });

  it('deve alternar estado de abertura via toggle()', () => {
    component.toggle();
    expect(component.isOpen()).toBe(true);

    component.toggle();
    expect(component.isOpen()).toBe(false);
  });

  it('não deve abrir se disabled for verdadeiro', () => {
    component.disabled = true;
    component.toggle();
    expect(component.isOpen()).toBe(false);
  });

  it('deve filtrar itens de acordo com permissões do usuário', () => {
    authServiceMock.hasPermission.mockImplementation((perm: string) => perm === Permission.SALES_WRITE);

    component.items = [
      { label: 'Visualizar', action: vi.fn() },
      { label: 'Excluir', permission: Permission.SALES_WRITE, action: vi.fn() },
      { label: 'Gerenciar Usuários', permission: Permission.USER_MANAGE, action: vi.fn() },
    ];

    const visible = component.visibleItems();
    expect(visible.length).toBe(2);
    expect(visible.map((i) => i.label)).toEqual(['Visualizar', 'Excluir']);
  });

  it('deve executar a action do item e fechar o menu ao clicar', () => {
    const actionSpy = vi.fn();
    const item: ActionMenuItem = { label: 'Editar', action: actionSpy };
    component.dataContext = { id: 10 };
    component.open();

    const mockEvent = { stopPropagation: vi.fn() } as unknown as Event;
    component.onItemClick(item, mockEvent);

    expect(mockEvent.stopPropagation).toHaveBeenCalled();
    expect(actionSpy).toHaveBeenCalledWith({ id: 10 });
    expect(component.isOpen()).toBe(false);
  });

  it('deve fechar o menu ao pressionar Escape', () => {
    component.open();
    expect(component.isOpen()).toBe(true);

    const mockEvent = new KeyboardEvent('keydown', { key: 'Escape' });
    component.onKeyDown(mockEvent);

    expect(component.isOpen()).toBe(false);
  });

  it('deve atualizar visibleItems reativamente quando items mudar dinamicamente', () => {
    component.items = [{ label: 'Desativar', danger: true }];
    expect(component.visibleItems()[0].label).toBe('Desativar');
    expect(component.visibleItems()[0].danger).toBe(true);

    component.items = [{ label: 'Ativar', success: true }];
    expect(component.visibleItems()[0].label).toBe('Ativar');
    expect(component.visibleItems()[0].success).toBe(true);
  });
});
