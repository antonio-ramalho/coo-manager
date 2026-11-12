import { Routes } from '@angular/router';
import { SalesShellComponent } from './pages/sales-shell/sales-shell.component';

export const SALES_ROUTES: Routes = [
  {
    path: '',
    component: SalesShellComponent,
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./pages/sales-page/sales-page.component').then((m) => m.SalesPageComponent),
      },
      {
        path: 'cadastro',
        loadComponent: () =>
          import('./pages/new-sale/new-sale.component').then((m) => m.NewSaleComponent),
        data: { hideSubNav: true },
      },
      {
        path: 'estatisticas',
        loadComponent: () =>
          import('./pages/sales-dashboard/sales-dashboard.component').then(
            (m) => m.SalesDashboardComponent,
          ),
      },
    ],
  },
];
