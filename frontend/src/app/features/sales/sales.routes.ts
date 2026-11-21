import { Routes } from '@angular/router';
import { SalesShellComponent } from './pages/sales-shell/sales-shell.component';
import { permissionGuard } from '../../core/guards/permission.guard';

export const SALES_ROUTES: Routes = [
  {
    path: '',
    component: SalesShellComponent,
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./pages/sales-page/sales-page.component').then((m) => m.SalesPageComponent),
        canActivate: [permissionGuard],
        data: { permission: 'SALES_READ' },
      },
      {
        path: 'cadastro',
        loadComponent: () =>
          import('./pages/new-sale/new-sale.component').then((m) => m.NewSaleComponent),
        canActivate: [permissionGuard],
        data: { permission: 'SALES_WRITE', hideSubNav: true },
      },
      {
        path: 'estatisticas',
        loadComponent: () =>
          import('./pages/sales-dashboard/sales-dashboard.component').then(
            (m) => m.SalesDashboardComponent,
          ),
        canActivate: [permissionGuard],
        data: { permission: 'SALES_READ' },
      },
    ],
  },
];
