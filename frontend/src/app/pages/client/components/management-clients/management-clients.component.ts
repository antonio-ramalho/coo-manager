import { Component } from '@angular/core';
import { ClientDetailsComponent } from '../client-details/client-details.component';
import { ButtonComponent } from '../../../../shared/components/button/button';
import { ClientsListComponent } from '../../../../shared/components/clients-list-component/clients-list-component';

@Component({
  selector: 'app-management-clients',
  standalone: true,
  imports: [ClientDetailsComponent, ClientsListComponent, ButtonComponent],
  templateUrl: './management-clients.component.html',
  host: { class: 'flex-1 min-h-0 flex flex-col w-full' },
})
export class ManagementClientsComponent {}
