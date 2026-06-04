import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../../../../../../shared/components/button/button';
import {
  LucideCalendar,
  LucideDownload,
  LucideFileText,
  LucideHouse,
  LucideMail,
  LucideMapPin,
  LucideMessageCircle,
  LucidePhone,
  LucideSquarePen,
  LucideUser,
} from '@lucide/angular';

@Component({
  selector: 'app-client-details',
  standalone: true,
  imports: [
    CommonModule,
    ButtonComponent,
    LucideMessageCircle,
    LucideSquarePen,
    LucideUser,
    LucideCalendar,
    LucidePhone,
    LucideMail,
    LucideFileText,
    LucideHouse,
    LucideMapPin,
    LucideDownload,
  ],
  templateUrl: './client-details-component.html',
})
export class ClientDetailsComponent {
  cliente = {
    nome: 'Ana Barreto',
    cpf: '345.876.234-11',
    tipo: 'AGRICULTOR',
    infoBasica: {
      genero: 'Feminino',
      nascimento: '01 de fevereiro de 2007',
      celular: '+55 (42) 98427-3174',
      email: 'ana.barreto@gmail.com',
    },
    caf: { numero: '8934.123', validade: '09/09/2026' },
    certificado: { numero: 'ORG-554', validade: '09/09/2026' },
    endereco: {
      cidade: 'Reserva do Iguaçu',
      rua: 'Segredo II',
      bairro: 'Segredo II',
      estado: 'Paraná',
    },
    anexos: [
      { nome: 'Cpf_pdf.pdf', tamanho: '2.3 mb' },
      { nome: 'caf.pdf', tamanho: '2.3 mb' },
      { nome: 'registro.pdf', tamanho: '2.3 mb' },
    ],
  };
}
