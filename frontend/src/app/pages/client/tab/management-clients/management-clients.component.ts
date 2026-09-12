import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { ClientDetailsComponent } from './components/client-details/client-details.component';
import { ClientListComponent } from './components/client-list/client-list.component';
import { ButtonComponent } from '../../../../shared/components/button/button';
import { ClienteService, Client } from '../../services/client.service';

@Component({
  selector: 'app-management-clients',
  standalone: true,
  imports: [ClientDetailsComponent, ClientListComponent, ButtonComponent],
  templateUrl: './management-clients.component.html',
  host: { class: 'flex-1 min-h-0 flex flex-col w-full' },
})
export class ManagementClientsComponent implements OnInit {
  private cdr = inject(ChangeDetectorRef);
  private clientService = inject(ClienteService);

  clientList: Client[] = [];

  ngOnInit(): void {
    this.loadingClients();
  }

  loadingClients(): void {
    this.clientService.obterClientes().subscribe({
      next: (data) => {
        this.clientList = data;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.log('Clients not loaded: ' + error);
      },
    });
  }
}
