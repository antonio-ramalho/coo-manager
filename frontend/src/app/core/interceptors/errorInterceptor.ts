import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastService } from '../services/toast-service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 0) {
        toastService.showError('Erro de conexão. Verifique sua internet.');
      } else if (error.status === 400 || error.status === 404) {
        const msg = error.error?.message || 'Dados inválidos ou não encontrados.';
        toastService.showWarning(msg);
      } else if (error.status >= 500) {
        toastService.showError('Erro interno no servidor. Tente novamente mais tarde.');
      }

      return throwError(() => error);
    }),
  );
};
