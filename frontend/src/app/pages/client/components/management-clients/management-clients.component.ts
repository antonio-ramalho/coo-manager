import { Component, inject, OnInit } from '@angular/core';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { GenericListComponent } from '../../../../shared/components/lists/generic-list/generic-list-component';
import { BehaviorSubject, combineLatest, filter, map, Observable, switchMap, tap } from 'rxjs';
import { ISimpleListCard } from '../../../../core/interfaces/ISimpleListCard';
import { ClientApiService } from '../../../../core/api/client-api';
import { ToastService } from '../../../../core/services/toast-service';
import { AsyncPipe } from '@angular/common';
import { CpfCnpjPipe } from '../../../../shared/pipes/cpf-cnpj-pipe';
import { RouterLink } from '@angular/router';
import { DetailsLayoutComponent } from '../../../../shared/layouts/details-layout/details-layout-component';
import { PersonDetailsHeaderComponent } from '../../../../shared/components/data-display/person-details-header/person-details-header';
import { PersonAddressInfoComponent } from '../../../../shared/components/data-display/address-info/address-info';
import { PersonBasicInfoCardComponent } from '../../../../shared/components/data-display/person-info/person-info';
import { AttachmentsCardComponent } from '../../../../shared/components/data-display/attachments-info/attachments-info';
import { ClientDetailsView, IClient, IJuridicClientDto, INaturalClientDto, } from '../../../../core/interfaces/IClient';

@Component({
  selector: 'app-management-clients',
  standalone: true,
  imports: [
    GenericListComponent,
    ButtonComponent,
    AsyncPipe,
    RouterLink,
    DetailsLayoutComponent,
    PersonDetailsHeaderComponent,
    PersonAddressInfoComponent,
    PersonBasicInfoCardComponent,
    AttachmentsCardComponent,
  ],
  providers: [CpfCnpjPipe],
  templateUrl: './management-clients.component.html',
  styleUrls: ['./management-clients.component.scss'],
})
export class ManagementClientsComponent implements OnInit {
  private currentPageSubject = new BehaviorSubject<number>(1);
  private searchSubject = new BehaviorSubject<string>('');
  private selectedClientIdSubject = new BehaviorSubject<number | null>(null);
  private clientService = inject(ClientApiService);
  private toastService = inject(ToastService);
  private cpfCnpjPipe = inject(CpfCnpjPipe);

  page$!: Observable<{ content: ISimpleListCard[]; totalElements: number }>;
  currentPage: number = 1;

  clientDetails$ = this.selectedClientIdSubject.pipe(
    filter((id) => id !== null),
    switchMap((id) => this.clientService.findById(id)),
    map((client: IClient) => this.mapToViewModel(client)),
  );

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
          isActive: client.status === 'ACTIVE',
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

  activateClient(id: number): void {
    this.clientService.activate(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Cliente ativado com sucesso.');
        this.currentPageSubject.next(this.currentPage);
      },
      error: (erro) => {
        this.toastService.showError('Erro ao ativar o cliente.');
        console.error(erro);
      },
    });
  }

  private mapToViewModel(client: IClient): ClientDetailsView {
    const isNatural = client.personType === 'NATURAL';
    const natural = client as INaturalClientDto;
    const juridic = client as IJuridicClientDto;

    return {
      id: client.id,
      legalName: client.legalName,
      status: client.status,
      personType: client.personType,
      phone: client.phone,
      email: client.email,
      address: client.address,
      attachments: [],

      documentNumber: isNatural ? natural.cpf : juridic.cnpj,
      documentLabel: isNatural ? 'Nº CPF' : 'Nº CNPJ',
      dateValue: isNatural ? natural.birthDate : juridic.foundationDate,
      dateLabel: isNatural ? 'Data de nascimento' : 'Data de fundação',
      gender: isNatural ? natural.gender : null,
      tradeName: isNatural ? null : juridic.tradeName,
    };
  }
}
