import { TestBed } from '@angular/core/testing';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { describe, expect, it, beforeEach, vi } from 'vitest';
import { permissionGuard } from './permission.guard';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';

describe('permissionGuard', () => {
  let authServiceMock: { hasPermission: ReturnType<typeof vi.fn> };
  let toastServiceMock: { showError: ReturnType<typeof vi.fn> };
  let routerMock: {
    navigate: ReturnType<typeof vi.fn>;
    getCurrentNavigation: ReturnType<typeof vi.fn>;
    url: string;
  };

  beforeEach(() => {
    authServiceMock = {
      hasPermission: vi.fn(),
    };
    toastServiceMock = {
      showError: vi.fn(),
    };
    routerMock = {
      navigate: vi.fn(),
      getCurrentNavigation: vi.fn().mockReturnValue(null),
      url: '/',
    };

    TestBed.configureTestingModule({
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: ToastService, useValue: toastServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    });
  });

  it('deve permitir acesso livre se a rota não exigir nenhuma permissão', () => {
    const route = { data: {} } as unknown as ActivatedRouteSnapshot;

    const result = TestBed.runInInjectionContext(() =>
      permissionGuard(route, {} as RouterStateSnapshot),
    );

    expect(result).toBe(true);
    expect(authServiceMock.hasPermission).not.toHaveBeenCalled();
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('deve permitir acesso quando o usuário possuir a permissão requerida', () => {
    authServiceMock.hasPermission.mockReturnValue(true);
    const route = { data: { permission: 'USER_MANAGE' } } as unknown as ActivatedRouteSnapshot;

    const result = TestBed.runInInjectionContext(() =>
      permissionGuard(route, {} as RouterStateSnapshot),
    );

    expect(result).toBe(true);
    expect(authServiceMock.hasPermission).toHaveBeenCalledWith('USER_MANAGE');
    expect(routerMock.navigate).not.toHaveBeenCalled();
    expect(toastServiceMock.showError).not.toHaveBeenCalled();
  });

  it('deve bloquear acesso e redirecionar para dashboard em acesso direto inicial quando usuário não possuir a permissão', () => {
    authServiceMock.hasPermission.mockReturnValue(false);
    routerMock.url = '/';
    const route = { data: { permission: 'USER_MANAGE' } } as unknown as ActivatedRouteSnapshot;

    const result = TestBed.runInInjectionContext(() =>
      permissionGuard(route, {} as RouterStateSnapshot),
    );

    expect(result).toBe(false);
    expect(authServiceMock.hasPermission).toHaveBeenCalledWith('USER_MANAGE');
    expect(toastServiceMock.showError).toHaveBeenCalledWith(
      'Acesso bloqueado: Seu perfil não possui a permissão requerida.',
    );
    expect(routerMock.navigate).toHaveBeenCalledWith(['/home/dashboard']);
  });

  it('deve bloquear acesso e manter o usuário na mesma página (sem redirecionar) quando já estiver navegando em uma rota válida', () => {
    authServiceMock.hasPermission.mockReturnValue(false);
    routerMock.url = '/home/vendas';
    const route = { data: { permission: 'SALES_CREATE' } } as unknown as ActivatedRouteSnapshot;

    const result = TestBed.runInInjectionContext(() =>
      permissionGuard(route, {} as RouterStateSnapshot),
    );

    expect(result).toBe(false);
    expect(authServiceMock.hasPermission).toHaveBeenCalledWith('SALES_CREATE');
    expect(toastServiceMock.showError).toHaveBeenCalledWith(
      'Acesso bloqueado: Seu perfil não possui a permissão requerida.',
    );
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });

  it('deve permitir acesso quando o usuário possuir pelo menos uma permissão da lista "permissions"', () => {
    authServiceMock.hasPermission.mockImplementation((perm: string) => perm === 'SALES_READ');
    const route = {
      data: { permissions: ['INPUT_READ', 'SALES_READ'] },
    } as unknown as ActivatedRouteSnapshot;

    const result = TestBed.runInInjectionContext(() =>
      permissionGuard(route, {} as RouterStateSnapshot),
    );

    expect(result).toBe(true);
    expect(routerMock.navigate).not.toHaveBeenCalled();
  });
});
