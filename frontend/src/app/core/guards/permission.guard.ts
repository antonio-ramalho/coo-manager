import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';

/**
 * Guarda de rota funcional para controle de acesso baseado em permissões granulares (RBAC).
 */
export const permissionGuard: CanActivateFn = (route) => {
  const authService = inject(AuthService);
  const toastService = inject(ToastService);
  const router = inject(Router);

  const requiredPermission = route.data?.['permission'] as string | undefined;
  const requiredPermissions = route.data?.['permissions'] as string[] | undefined;

  if (!requiredPermission && (!requiredPermissions || requiredPermissions.length === 0)) {
    return true;
  }

  let hasAccess = false;
  if (requiredPermission) {
    hasAccess = authService.hasPermission(requiredPermission);
  } else if (requiredPermissions) {
    hasAccess = requiredPermissions.some((p) => authService.hasPermission(p));
  }

  if (hasAccess) {
    return true;
  }

  toastService.showError('Acesso bloqueado: Seu perfil não possui a permissão requerida.');

  const currentNav = router.getCurrentNavigation();
  const hasPreviousNavigation = !!currentNav?.previousNavigation;

  if (hasPreviousNavigation || (router.url && router.url !== '/' && !router.url.startsWith('/login'))) {
    return false;
  }

  router.navigate(['/home/dashboard']);
  return false;
};

