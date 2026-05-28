import {Routes} from '@angular/router';
import {HomeComponent} from './pages/home/home';
import {DashboardComponent} from './pages/home/components/dashboard/dashboard';
import {LogisticsComponent} from './pages/home/components/logistics/logistics';
import {SalesComponent} from './pages/home/components/sales/sales';
import {RegistrationsComponent} from './pages/home/components/registrations/registrations';
import {FinancialComponent} from './pages/home/components/financial/financial';

export const routes: Routes = [
  {
    path : "home",
    component : HomeComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'logistica', component: LogisticsComponent },
      { path: 'vendas', component: SalesComponent },
      { path: 'financeiro', component: FinancialComponent },
      { path: 'cadastros', component: RegistrationsComponent }
    ]
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  }
];
