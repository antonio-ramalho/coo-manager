import { Component, Input } from '@angular/core';
import { LucideCake, LucideMail, LucidePhone, LucideVenus } from '@lucide/angular';
import { DatePipe } from '@angular/common';
import { PhonePipe } from '../../../pipes/phone-pipe';
import { CpfCnpjPipe } from '../../../pipes/cpf-cnpj-pipe';
import { GenderPipe } from '../../../pipes/gender-pipe';

@Component({
  selector: 'app-person-basic-info-card',
  imports: [
    LucideCake,
    LucideMail,
    LucidePhone,
    LucideVenus,
    DatePipe,
    PhonePipe,
    CpfCnpjPipe,
    GenderPipe,
  ],
  templateUrl: './person-info.html',
})
export class PersonBasicInfoCardComponent {
  @Input() gender?: string | null;
  @Input() phone!: string;
  @Input() email!: string;
  @Input() dataLabel: string = 'Data de nascimento';
  @Input() dataValue!: string;
  @Input() documentLabel!: string;
  @Input() documentNumber!: string;
  @Input() personType!: string;
}
