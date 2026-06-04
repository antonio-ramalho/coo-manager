import { Component } from '@angular/core';
import { ClientDetailsComponent } from './components/client-details/client-details-component';
import { ClientListComponent } from './components/client-list/client-list-component';

@Component({
  selector: 'app-management-clients',
  standalone: true,
  imports: [ClientDetailsComponent, ClientListComponent],
  templateUrl: './management-clients.component.html',
})
export class ManagementClientsComponent {}
