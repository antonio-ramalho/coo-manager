import { Component } from '@angular/core';
import { ButtonComponent } from '../../../../../../shared/components/button/button';
import { CommonModule } from '@angular/common';
import {
  LucideArrowRight,
  LucideFunnel,
  LucideEllipsis,
  LucideSearch
} from '@lucide/angular';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [
    CommonModule,
    ButtonComponent,
    LucideSearch,
    LucideFunnel,
    LucideEllipsis,
    LucideArrowRight,
  ],
  templateUrl: './client-list-component.html',
})
export class ClientListComponent {
  // Lista simulada para testar o visual e a rolagem (scroll)
  // Futuramente, isto será substituído por um @Input() ou chamada ao Service
  clientes = [
    { nome: 'Ana Barreto', cpf: '345.876.234-11', selecionado: true },
    { nome: 'João Silva', cpf: '123.456.789-00', selecionado: false },
    { nome: 'Carlos Mendes', cpf: '987.654.321-99', selecionado: false },
    { nome: 'Marta Ribeiro', cpf: '456.123.789-44', selecionado: false },
    { nome: 'Ana Barreto', cpf: '345.876.234-11', selecionado: true },
  ];

  // Método simples para simular o clique e a seleção
  selecionarCliente(clienteClicado: any) {
    this.clientes.forEach((c) => (c.selecionado = false)); // Remove a seleção de todos
    clienteClicado.selecionado = true; // Aplica no que foi clicado
  }
}
