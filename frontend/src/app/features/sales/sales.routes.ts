import { Routes } from '@angular/router';
import { SalesShellComponent } from './pages/sales-shell/sales-shell.component';

export const SALES_ROUTES: Routes = [
  {
    path: '',
    component: SalesShellComponent,
    children: [
      { path: '', redirectTo: 'nova-venda', pathMatch: 'full' },
      {
        path: 'nova-venda',
        loadComponent: () =>
          import('./pages/new-sale/new-sale.component').then((m) => m.NewSaleComponent),
      },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./pages/sales-dashboard/sales-dashboard.component').then(
            (m) => m.SalesDashboardComponent,
          ),
      },
    ],
  },
];
