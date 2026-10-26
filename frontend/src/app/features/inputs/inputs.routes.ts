import { Routes } from '@angular/router';
import { InputsShellComponent } from './pages/inputs-shell/inputs-shell.component';
import { InputStockComponent } from './pages/input-stock/input-stock.component';
import { InputCatalogComponent } from './pages/input-catalog/input-catalog.component';
import { InputProductFormComponent } from './pages/input-product-form/input-product-form.component';
import { InputBatchFormComponent } from './pages/input-batch-form/input-batch-form.component';

export const INPUTS_ROUTES: Routes = [
  {
    path: '',
    component: InputsShellComponent,
    children: [
      { path: '', redirectTo: 'estoque', pathMatch: 'full' },
      { path: 'estoque', component: InputStockComponent },
      { path: 'insumos-estoque', redirectTo: 'estoque', pathMatch: 'full' },
      { path: 'catalogo', component: InputCatalogComponent },
      { path: 'cadastro', component: InputProductFormComponent, data: { hideSubNav: true } },
      { path: 'editar/:id', component: InputProductFormComponent, data: { hideSubNav: true } },
      { path: 'lotes/cadastro', component: InputBatchFormComponent, data: { hideSubNav: true } },
      { path: 'lotes/editar/:id', component: InputBatchFormComponent, data: { hideSubNav: true } },
    ],
  },
];
