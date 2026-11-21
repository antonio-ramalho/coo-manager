import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const toastService = inject(ToastService);
  const router = inject(Router);

  const token = authService.accessToken();

  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  }

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        if (!req.url.includes('/auth/login') && !req.url.includes('/auth/2fa/verify')) {
          authService.logout();
          toastService.showWarning('Sua sessão expirou. Faça login novamente.');
        }
      } else if (error.status === 403) {
        toastService.showError('Acesso negado. Você não possui permissão para executar esta ação.');
      }

      return throwError(() => error);
    }),
  );
};
