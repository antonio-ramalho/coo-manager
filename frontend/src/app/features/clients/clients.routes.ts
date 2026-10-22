import { Routes } from '@angular/router';
import { ClientsShellComponent } from './pages/clients-shell/clients-shell.component';
import { ClientsManagementComponent } from './pages/clients-management/clients-management.component';
import { ClientFormComponent } from './pages/client-form/client-form.component';

export const CLIENTS_ROUTES: Routes = [
  {
    path: '',
    component: ClientsShellComponent,
    children: [
      { path: '', redirectTo: 'gestao', pathMatch: 'full' },
      { path: 'gestao', component: ClientsManagementComponent },
      { path: 'cadastro', component: ClientFormComponent, data: { hideSubNav: true } },
      { path: 'editar/:id', component: ClientFormComponent, data: { hideSubNav: true } },
    ],
  },
];
