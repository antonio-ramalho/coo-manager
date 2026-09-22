import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PersonBasicInfoCardComponent } from '../../../../shared/components/person-basic-info-card/person-basic-info-card';
import { IJuridicClientDto, INaturalClientDto } from '../../../../core/interfaces/IClient';
import { PersonDetailsHeaderComponent } from '../../../../shared/components/person-details-header/person-details-header';
import { PersonAddressInfoComponent } from '../../../../shared/components/person-address-info/person-address-info';
import { IAttachments } from '../../../../core/interfaces/IAttachments';
import { AttachmentsCardComponent } from '../../../../shared/components/attachments-card/attachments-card';

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [
    CommonModule,
    PersonDetailsHeaderComponent,
    PersonBasicInfoCardComponent,
    PersonAddressInfoComponent,
    AttachmentsCardComponent,
  ],
  templateUrl: './client-details.component.html',
})
export class ClientDetailsComponent {
  @Input() person!: INaturalClientDto | IJuridicClientDto;

  get documentNumber(): string {
    if (this.person.personType === 'NATURAL') {
      return (this.person as INaturalClientDto).cpf;
    } else {
      return (this.person as IJuridicClientDto).cnpj;
    }
  }

  get documentLabel(): string {
    return this.person.personType === 'NATURAL' ? 'Nº Cpf' : 'Nº Cnpj';
  }

  get gender(): string | null {
    if (this.person.personType === 'NATURAL') {
      return (this.person as INaturalClientDto).gender;
    } else {
      return null;
    }
  }

  get dateLabel(): string {
    return this.person.personType === 'NATURAL' ? 'Data de nascimento' : 'Data de fundação';
  }

  get dateValue(): string {
    if (this.person.personType === 'NATURAL') {
      return (this.person as INaturalClientDto).birthDate;
    } else {
      return (this.person as IJuridicClientDto).foundationDate;
    }
  }

  get attachments(): IAttachments[] {
    return [];
  }
}
