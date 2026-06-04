import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LogisticsComponent } from './pages/logistics/logistics.component';
import { SalesComponent } from './pages/sales/sales.component';
import { FinancialComponent } from './pages/financial/financial.component';
import { ClientsComponent } from './pages/client/client.component';
import { CustomerDemographicsComponent } from './pages/client/tab/customer-demographics/customer-demographics.component';
import { ManagementClientsComponent } from './pages/client/tab/management-clients/management-clients.component';

export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
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
          { path: 'demograficos', component: CustomerDemographicsComponent },
          { path: '', redirectTo: 'gestao', pathMatch: 'full' },
        ],
      },
    ],
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
];
