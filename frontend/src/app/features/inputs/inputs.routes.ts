import { Routes } from '@angular/router';
import { InputsShellComponent } from './pages/inputs-shell/inputs-shell.component';
import { InputStockComponent } from './pages/input-stock/input-stock.component';
import { InputCatalogComponent } from './pages/input-catalog/input-catalog.component';
import { InputFormComponent } from './pages/input-form/input-form.component';

export const INPUTS_ROUTES: Routes = [
  {
    path: '',
    component: InputsShellComponent,
    children: [
      { path: '', redirectTo: 'estoque', pathMatch: 'full' },
      { path: 'estoque', component: InputStockComponent },
      { path: 'insumos-estoque', redirectTo: 'estoque', pathMatch: 'full' },
      { path: 'catalogo', component: InputCatalogComponent },
      { path: 'cadastro', component: InputFormComponent, data: { hideSubNav: true } },
      { path: 'editar/:id', component: InputFormComponent, data: { hideSubNav: true } },
    ],
  },
];
