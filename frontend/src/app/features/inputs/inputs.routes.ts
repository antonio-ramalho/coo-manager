import { Routes } from '@angular/router';
import { InputsShellComponent } from './pages/inputs-shell/inputs-shell.component';
import { InputStockComponent } from './pages/input-stock/input-stock.component';
import { InputCatalogComponent } from './pages/input-catalog/input-catalog.component';
import { permissionGuard } from '../../core/guards/permission.guard';
import { Permission } from '../../shared/enums/permission.enum';

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
        data: { permission: Permission.INPUT_READ },
      },
      { path: 'insumos-estoque', redirectTo: 'estoque', pathMatch: 'full' },
      {
        path: 'catalogo',
        component: InputCatalogComponent,
        canActivate: [permissionGuard],
        data: { permission: Permission.INPUT_READ },
      },
    ],
  },
];
