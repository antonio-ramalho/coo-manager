import { Routes } from '@angular/router';
import { AdminLayoutComponent } from './core/layout/admin-layout/admin-layout.component';
import { authGuard } from './core/guards/auth.guard';
import { permissionGuard } from './core/guards/permission.guard';
import { Permission } from './shared/enums/permission.enum';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/pages/login-page/login-page.component').then(
        (m) => m.LoginPageComponent,
      ),
  },
  {
    path: 'perfil',
    loadComponent: () =>
      import('./features/profile/pages/profile-page/profile-page.component').then(
        (m) => m.ProfilePageComponent,
      ),
    canActivate: [authGuard],
  },
  {
    path: 'home',
    component: AdminLayoutComponent,
    canActivate: [authGuard],
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
        canActivate: [permissionGuard],
        data: { permission: Permission.LOGISTICS_READ },
      },
      {
        path: 'vendas',
        loadChildren: () =>
          import('./features/sales/sales.routes').then((m) => m.SALES_ROUTES),
        canActivate: [permissionGuard],
        data: { permission: Permission.SALES_READ },
      },
      {
        path: 'financeiro',
        loadComponent: () =>
          import('./features/financial/pages/financial-page/financial-page.component').then(
            (m) => m.FinancialPageComponent,
          ),
        canActivate: [permissionGuard],
        data: { permission: Permission.FINANCIAL_READ },
      },
      {
        path: 'clientes',
        loadChildren: () =>
          import('./features/clients/clients.routes').then((m) => m.CLIENTS_ROUTES),
        canActivate: [permissionGuard],
        data: { permission: Permission.CLIENT_READ },
      },
      {
        path: 'insumos',
        loadChildren: () =>
          import('./features/inputs/inputs.routes').then((m) => m.INPUTS_ROUTES),
        canActivate: [permissionGuard],
        data: { permission: Permission.INPUT_READ },
      },
      {
        path: 'cooperativa',
        loadComponent: () =>
          import(
            './features/cooperatives/pages/cooperative-page/cooperative-page.component'
          ).then((m) => m.CooperativePageComponent),
        canActivate: [permissionGuard],
        data: { permission: Permission.COOPERATIVE_READ },
      },
      {
        path: 'funcionarios',
        loadComponent: () =>
          import(
            './features/employees/pages/employees-page/employees-page.component'
          ).then((m) => m.EmployeesPageComponent),
        canActivate: [permissionGuard],
        data: { permission: Permission.USER_MANAGE },
      },
      {
        path: 'perfil',
        redirectTo: '/perfil',
        pathMatch: 'full',
      },
    ],
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: '**',
    redirectTo: 'home',
  },
];
