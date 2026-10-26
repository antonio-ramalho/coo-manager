import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { BehaviorSubject, combineLatest, filter, map, Observable, switchMap } from 'rxjs';

import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { GenericListComponent } from '../../../../shared/components/lists/generic-list/generic-list-component';
import { DesktopOnlyNoticeComponent } from '../../../../shared/components/feedback/desktop-only-notice/desktop-only-notice.component';
import { ClientDetailsPanelComponent } from '../../components/client-details-panel/client-details-panel.component';

import { ListCardItem } from '../../../../shared/models/list-card-item.model';
import { ClientApiService } from '../../data/client-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { Client, ClientDetails } from '../../models/client.model';
import { toClientDetails, toListCardItem } from '../../mappers/client.mapper';

/**
 * Smart Component para a Gestão de Clientes.
 * Aplica degradação graciosa com `@if (isDesktop())`: em telas mobile, a listagem e as
 * requisições de rede não são ativadas, exibindo o aviso amigável DesktopOnlyNotice.
 */
@Component({
  selector: 'app-clients-management',
  imports: [
    GenericListComponent,
    ButtonComponent,
    AsyncPipe,
    RouterLink,
    DesktopOnlyNoticeComponent,
    ClientDetailsPanelComponent,
  ],
  templateUrl: './clients-management.component.html',
  styleUrls: ['./clients-management.component.scss'],
})
export class ClientsManagementComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly clientService = inject(ClientApiService);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);

  /** Identifica se o dispositivo possui largura suficiente (>= 768px). */
  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  private readonly currentPageSubject = new BehaviorSubject<number>(1);
  private readonly searchSubject = new BehaviorSubject<string>('');
  private readonly selectedClientIdSubject = new BehaviorSubject<number | null>(null);

  currentPage = 1;

  readonly page$: Observable<{ content: ListCardItem[]; totalElements: number }> = combineLatest([
    this.currentPageSubject,
    this.searchSubject,
  ]).pipe(
    switchMap(([page, searchTerm]) => {
      return this.clientService.findAllPaged(page - 1, 10, searchTerm);
    }),
    map((response) => ({
      content: response.content.map(toListCardItem),
      totalElements: response.totalElements,
    })),
  );

  readonly clientDetails$: Observable<ClientDetails> = this.selectedClientIdSubject.pipe(
    filter((id): id is number => id !== null),
    switchMap((id) => this.clientService.findById(id)),
    map((client: Client) => toClientDetails(client)),
  );

  applyFilters(searchTerm: string): void {
    const cleanTerm = searchTerm ? searchTerm.trim().replace(/[.\-/]/g, '') : '';
    this.currentPage = 1;
    this.currentPageSubject.next(1);
    this.searchSubject.next(cleanTerm);
  }

  swappedPage(newPage: number): void {
    this.currentPage = newPage;
    this.currentPageSubject.next(newPage);
  }

  viewDetails(id: number): void {
    this.selectedClientIdSubject.next(id);
  }

  editClient(id: number): void {
    void this.router.navigate(['/home/clientes/editar', id]);
  }

  deactivateClient(id: number): void {
    this.clientService.delete(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Cliente desativado com sucesso.');
        this.currentPageSubject.next(this.currentPage);
      },
      error: (error: unknown) => {
        this.toastService.showError('Erro ao desativar o cliente.');
        console.error(error);
      },
    });
  }

  activateClient(id: number): void {
    this.clientService.activate(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Cliente ativado com sucesso.');
        this.currentPageSubject.next(this.currentPage);
      },
      error: (error: unknown) => {
        this.toastService.showError('Erro ao ativar o cliente.');
        console.error(error);
      },
    });
  }
}
