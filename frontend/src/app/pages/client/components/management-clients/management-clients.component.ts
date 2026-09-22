import { Component, inject, OnInit } from '@angular/core';
import { ClientDetailsComponent } from '../client-details/client-details.component';
import { ButtonComponent } from '../../../../shared/components/button/button';
import { GenericListComponent } from '../../../../shared/components/generic-list/generic-list-component';
import { BehaviorSubject, combineLatest, filter, map, Observable, switchMap, tap } from 'rxjs';
import { ISimpleListCard } from '../../../../core/interfaces/ISimpleListCard';
import { ClientApiService } from '../../../../core/api/client-api';
import { ToastService } from '../../../../core/services/toast-service';
import { AsyncPipe } from '@angular/common';
import { CpfCnpjPipe } from '../../../../shared/pipes/cpf-cnpj-pipe';

@Component({
  selector: 'app-management-clients',
  standalone: true,
  imports: [ClientDetailsComponent, GenericListComponent, ButtonComponent, AsyncPipe],
  providers: [CpfCnpjPipe],
  templateUrl: './management-clients.component.html',
  styleUrls: ['./management-clients.component.scss'],
})
export class ManagementClientsComponent implements OnInit {
  private currentPageSubject = new BehaviorSubject<number>(1);
  private searchSubject = new BehaviorSubject<string>('');
  private selectedClientIdSubject = new BehaviorSubject<number | null>(null);

  clientDetails$ = this.selectedClientIdSubject.pipe(
    filter((id) => id !== null),
    switchMap((id) => this.clientService.findById(id)),
  );

  page$!: Observable<{ content: ISimpleListCard[]; totalElements: number }>;

  currentPage: number = 1;

  private clientService = inject(ClientApiService);
  private toastService = inject(ToastService);
  private cpfCnpjPipe = inject(CpfCnpjPipe);

  ngOnInit(): void {
    this.page$ = combineLatest([this.currentPageSubject, this.searchSubject]).pipe(
      switchMap(([page, searchTerm]) => {
        return this.clientService.findAllPaged(page - 1, 10, searchTerm);
      }),
      map((response) => {
        const genericContent: ISimpleListCard[] = response.content.map((client) => ({
          id: client.id,
          title: client.legalName,
          subtitle: this.cpfCnpjPipe.transform(client.documentNumber),
        }));

        return {
          content: genericContent,
          totalElements: response.totalElements,
        };
      }),
    );
  }

  applyFilters(searchTerm: any): void {
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

  editClient(id: number | string): void {
    console.log('Abrir modal de edição do cliente:', id);
  }

  deactivateClient(id: number): void {
    this.clientService.delete(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Cliente desativado com sucesso.');

        this.currentPageSubject.next(this.currentPage);
      },
      error: (erro) => {
        this.toastService.showError('Erro ao desativar o cliente.');
        console.error(erro);
      },
    });
  }
}
