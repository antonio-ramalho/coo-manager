import { Routes } from '@angular/router';
import { BasePageComponent } from './shared/layouts/base-page/base-page.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LogisticsComponent } from './pages/logistics/logistics.component';
import { SalesComponent } from './pages/sales/sales.component';
import { FinancialComponent } from './pages/financial/financial.component';
import { ClientsComponent } from './pages/client/client.component';
import { ManagementClientsComponent } from './pages/client/components/management-clients/management-clients.component';
import { UniversalFormComponent } from './pages/universal-form/universal-form-component';
import { InputsProductsComponent } from './pages/inputs-products/inputs-products-component';
import { InputStockComponent } from './pages/inputs-products/components/input-stock/input-stock';
import {InputCatalogComponent} from './pages/inputs-products/components/input-catalog/input-catalog-component';

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
          { path: 'cadastro', component: UniversalFormComponent, data: { type: 'cliente' } },
          { path: 'editar/:id', component: UniversalFormComponent, data: { type: 'cliente' } },
          { path: '', redirectTo: 'gestao', pathMatch: 'full' },
        ],
      },
      {
        path: 'insumos',
        component: InputsProductsComponent,
        children: [
          { path: 'insumos-estoque', component: InputStockComponent },
          { path: 'cadastro', component: UniversalFormComponent, data: { type: 'insumo' } },
          { path: 'editar/:id', component: UniversalFormComponent, data: { type: 'insumo' } },
          { path: 'catalogo', component: InputCatalogComponent },
          { path: '', redirectTo: 'insumos-estoque', pathMatch: 'full' },
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
