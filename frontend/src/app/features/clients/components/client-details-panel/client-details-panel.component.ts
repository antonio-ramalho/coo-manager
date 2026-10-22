import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DetailsPanelComponent } from '../../../../shared/components/layout/details-panel/details-panel.component';
import { PersonDetailsHeaderComponent } from '../../../../shared/components/data-display/person-details-header/person-details-header';
import { PersonBasicInfoCardComponent } from '../../../../shared/components/data-display/person-info/person-info';
import { PersonAddressInfoComponent } from '../../../../shared/components/data-display/address-info/address-info';
import { AttachmentsCardComponent } from '../../../../shared/components/data-display/attachments-info/attachments-info';
import { ClientDetails } from '../../models/client.model';

/**
 * Dumb Component responsável por exibir as seções de detalhes de um cliente selecionado.
 */
@Component({
  selector: 'app-client-details-panel',
  imports: [
    DetailsPanelComponent,
    PersonDetailsHeaderComponent,
    PersonBasicInfoCardComponent,
    PersonAddressInfoComponent,
    AttachmentsCardComponent,
  ],
  templateUrl: './client-details-panel.component.html',
  host: {
    '[style.height]': '"100%"'
  }
})
export class ClientDetailsPanelComponent {
  @Input({ required: true }) details!: ClientDetails;
  @Output() edit = new EventEmitter<number>();
}
