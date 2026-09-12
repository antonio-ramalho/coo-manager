import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonComponent } from '../../../../../../shared/components/button/button';
import {
  LucideSearch,
  LucideArrowRight,
  LucideArrowLeft,
  LucideX,
  LucideFunnel,
  LucideEllipsis,
} from '@lucide/angular';
import { Client} from '../../../../services/client.service';

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
    LucideArrowLeft,
    LucideX,
  ],
  templateUrl: './client-list.component.html',
})
export class ClientListComponent {
  isSearchOpen = false;
  currentPage = 1;
  itensPerPage = 4;
  pagesPerBlock = 3;
  currentBlock = 0;

  visibleClients: Client[] = [];
  allClients: Client[] = [];

  @Input() set clients(clients: Client[]) {
    if (clients) {
      this.allClients = clients;
      this.currentPage = 1;
      this.currentBlock = 0;
      this.atualizarLista();
    }
  }

  toggleSearch(): void {
    this.isSearchOpen = !this.isSearchOpen;
  }

  selecionarCliente(clienteSelecionado: Client): void {
    this.allClients.forEach((c) => (c.selecionado = false));
    clienteSelecionado.selecionado = true;
  }

  get totalPages(): number {
    return Math.ceil(this.allClients.length / this.itensPerPage);
  }

  get paginasVisiveis(): number[] {
    const inicio = this.currentBlock * this.pagesPerBlock + 1;
    const fim = Math.min(inicio + this.pagesPerBlock - 1, this.totalPages);
    const paginas = [];
    for (let i = inicio; i <= fim; i++) {
      paginas.push(i);
    }
    return paginas;
  }

  get temProximoBloco(): boolean {
    return (this.currentBlock + 1) * this.pagesPerBlock < this.totalPages;
  }

  mudarPagina(pagina: number): void {
    this.currentPage = pagina;
    this.atualizarLista();
  }

  proximoBloco(): void {
    if (this.temProximoBloco) {
      this.currentBlock++;
    }
  }

  blocoAnterior(): void {
    if (this.currentBlock > 0) {
      this.currentBlock--;
    }
  }

  atualizarLista(): void {
    const indexInicio = (this.currentPage - 1) * this.itensPerPage;
    const indexFim = indexInicio + this.itensPerPage;
    this.visibleClients = this.allClients.slice(indexInicio, indexFim);
  }
}
