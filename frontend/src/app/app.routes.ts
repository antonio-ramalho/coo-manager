import { Routes } from '@angular/router';
import { BasePageComponent } from './shared/layouts/base-page/base-page.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LogisticsComponent } from './pages/logistics/logistics.component';
import { SalesComponent } from './pages/sales/sales.component';
import { FinancialComponent } from './pages/financial/financial.component';
import { ClientsComponent } from './pages/client/client.component';
import { ManagementClientsComponent } from './pages/client/components/management-clients/management-clients.component';
import { UniversalFormComponent } from './pages/universal-form/universal-form-component';

export const routes: Routes = [
  {
    path: 'home',
    component: BasePageComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'logistica', component: LogisticsComponent },
      { path: 'vendas', component: SalesComponent },
      { path: 'financeiro', component: FinancialComponent },
      {
        path: 'clientes',
        component: ClientsComponent,
        children: [
          { path: 'gestao', component: ManagementClientsComponent },
          { path: '', redirectTo: 'gestao', pathMatch: 'full' },
        ],
      },
      {
        path: 'cadastro/:tipo',
        component: UniversalFormComponent,
      },
    ],
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
];
