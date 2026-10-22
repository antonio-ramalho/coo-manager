import { HttpInterceptorFn } from '@angular/common/http';

const ACCESS_TOKEN_KEY = 'access_token';

/**
 * Anexa o token JWT às requisições.
 * Ainda não registrado em `app.config.ts`: será ativado junto com o fluxo de autenticação.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem(ACCESS_TOKEN_KEY);

  if (!token) {
    return next(req);
  }

  return next(req.clone({ setHeaders: { Authorization: `Bearer ${token}` } }));
};
