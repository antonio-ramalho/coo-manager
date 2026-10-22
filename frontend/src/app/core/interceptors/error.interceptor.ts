import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ToastService } from '../services/toast.service';

/** Status de regra de negócio/validação: a mensagem vem do backend e é exibida como aviso. */
const BUSINESS_WARNING_STATUSES = new Set([400, 404, 409, 422]);
const CONNECTION_ERROR_STATUS = 0;
const SERVER_ERROR_MIN_STATUS = 500;

/** Converte erros HTTP em notificações para o usuário e repassa o erro adiante. */
export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const toastService = inject(ToastService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      notify(error, toastService);
      return throwError(() => error);
    }),
  );
};

function notify(error: HttpErrorResponse, toastService: ToastService): void {
  if (error.status === CONNECTION_ERROR_STATUS) {
    toastService.showError('Erro de conexão. Verifique sua internet.');
    return;
  }

  if (BUSINESS_WARNING_STATUSES.has(error.status)) {
    toastService.showWarning(error.error?.message);
    return;
  }

  if (error.status >= SERVER_ERROR_MIN_STATUS) {
    toastService.showError('Erro interno no servidor. Tente novamente mais tarde.');
  }
}
