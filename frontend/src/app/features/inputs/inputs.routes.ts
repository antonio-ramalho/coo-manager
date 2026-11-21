import { Routes } from '@angular/router';
import { InputsShellComponent } from './pages/inputs-shell/inputs-shell.component';
import { InputStockComponent } from './pages/input-stock/input-stock.component';
import { InputCatalogComponent } from './pages/input-catalog/input-catalog.component';
import { InputProductFormComponent } from './pages/input-product-form/input-product-form.component';
import { InputBatchFormComponent } from './pages/input-batch-form/input-batch-form.component';
import { permissionGuard } from '../../core/guards/permission.guard';

export const INPUTS_ROUTES: Routes = [
  {
    path: '',
    component: InputsShellComponent,
    children: [
      { path: '', redirectTo: 'estoque', pathMatch: 'full' },
      {
        path: 'estoque',
        component: InputStockComponent,
        canActivate: [permissionGuard],
        data: { permission: 'INPUT_READ' },
      },
      { path: 'insumos-estoque', redirectTo: 'estoque', pathMatch: 'full' },
      {
        path: 'catalogo',
        component: InputCatalogComponent,
        canActivate: [permissionGuard],
        data: { permission: 'INPUT_READ' },
      },
      {
        path: 'cadastro',
        component: InputProductFormComponent,
        canActivate: [permissionGuard],
        data: { permission: 'INPUT_WRITE', hideSubNav: true },
      },
      {
        path: 'editar/:id',
        component: InputProductFormComponent,
        canActivate: [permissionGuard],
        data: { permission: 'INPUT_WRITE', hideSubNav: true },
      },
      {
        path: 'lotes/cadastro',
        component: InputBatchFormComponent,
        canActivate: [permissionGuard],
        data: { permission: 'INPUT_WRITE', hideSubNav: true },
      },
      {
        path: 'lotes/editar/:id',
        component: InputBatchFormComponent,
        canActivate: [permissionGuard],
        data: { permission: 'INPUT_WRITE', hideSubNav: true },
      },
    ],
  },
];
