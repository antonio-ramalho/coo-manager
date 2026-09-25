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
      } else if (error.status === 400 || error.status === 404 || error.status === 409) {
        const msg = error.error?.message;
        toastService.showWarning(msg);
      } else if (error.status >= 500) {
        toastService.showError('Erro interno no servidor. Tente novamente mais tarde.');
      }

      return throwError(() => error);
    }),
  );
};
