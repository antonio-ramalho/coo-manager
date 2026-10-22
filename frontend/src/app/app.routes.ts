import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './core/layout/admin-layout/admin-layout.component';

export const routes: Routes = [
  {
    path: 'home',
    component: AdminLayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadComponent: () =>
          import('./features/dashboard/pages/dashboard-page/dashboard-page.component').then(
            (m) => m.DashboardPageComponent,
          ),
      },
      {
        path: 'logistica',
        loadComponent: () =>
          import('./features/logistics/pages/logistics-page/logistics-page.component').then(
            (m) => m.LogisticsPageComponent,
          ),
      },
      {
        path: 'vendas',
        loadComponent: () =>
          import('./features/sales/pages/sales-page/sales-page.component').then(
            (m) => m.SalesPageComponent,
          ),
      },
      {
        path: 'financeiro',
        loadComponent: () =>
          import('./features/financial/pages/financial-page/financial-page.component').then(
            (m) => m.FinancialPageComponent,
          ),
      },
      {
        path: 'clientes',
        loadChildren: () =>
          import('./features/clients/clients.routes').then((m) => m.CLIENTS_ROUTES),
      },
      {
        path: 'insumos',
        loadChildren: () =>
          import('./features/inputs/inputs.routes').then((m) => m.INPUTS_ROUTES),
      },
    ],
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
];
