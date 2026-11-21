import { Routes } from '@angular/router';
import { ClientsShellComponent } from './pages/clients-shell/clients-shell.component';
import { ClientsManagementComponent } from './pages/clients-management/clients-management.component';
import { ClientFormComponent } from './pages/client-form/client-form.component';
import { permissionGuard } from '../../core/guards/permission.guard';
import { Permission } from '../../shared/enums/permission.enum';

export const CLIENTS_ROUTES: Routes = [
  {
    path: '',
    component: ClientsShellComponent,
    children: [
      { path: '', redirectTo: 'gestao', pathMatch: 'full' },
      {
        path: 'gestao',
        component: ClientsManagementComponent,
        canActivate: [permissionGuard],
        data: { permission: Permission.CLIENT_READ },
      },
      {
        path: 'cadastro',
        component: ClientFormComponent,
        canActivate: [permissionGuard],
        data: { permission: Permission.CLIENT_WRITE, hideSubNav: true },
      },
      {
        path: 'editar/:id',
        component: ClientFormComponent,
        canActivate: [permissionGuard],
        data: { permission: Permission.CLIENT_WRITE, hideSubNav: true },
      },
    ],
  },
];
