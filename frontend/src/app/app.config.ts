import { ApplicationConfig, provideBrowserGlobalErrorListeners, LOCALE_ID } from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { errorInterceptor } from './core/interceptors/error.interceptor';
import { provideEnvironmentNgxMask } from 'ngx-mask';
import { provideFlatpickrDefaults } from 'angularx-flatpickr';
import { Portuguese } from 'flatpickr/dist/l10n/pt.js';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { Chart } from 'chart.js';

registerLocaleData(localePt);
Chart.defaults.font.family = "'Roboto', ui-sans-serif, system-ui, sans-serif";

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(withInterceptors([errorInterceptor])),
    provideEnvironmentNgxMask(),
    provideFlatpickrDefaults({ locale: Portuguese }),
    { provide: LOCALE_ID, useValue: 'pt-BR' },
    provideCharts(withDefaultRegisterables()),
  ],
};
