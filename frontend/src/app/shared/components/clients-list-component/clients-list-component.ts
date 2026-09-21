import { IPage } from '../../../core/interfaces/IPage';
import { Component, inject, OnInit } from '@angular/core';
import { BehaviorSubject, combineLatest, switchMap, Observable } from 'rxjs';
import { ItemClientComponent } from '../item-client-component/item-client-component';
import { PaginationComponent } from '../pagination-component/pagination-component';
import { ClientApiService } from '../../../core/api/client-api';
import { IClientMinDto } from '../../../core/interfaces/IClient';
import { AsyncPipe } from '@angular/common';
import { SearchBarComponent } from '../search-bar-component/search-bar-component';

@Component({
  selector: 'app-clients-list-component',
  standalone: true,
  imports: [ItemClientComponent, PaginationComponent, AsyncPipe, SearchBarComponent],
  templateUrl: './clients-list-component.html',
})
export class ClientsListComponent implements OnInit {
  private currentPageSubject = new BehaviorSubject<number>(1);
  private searchSubject = new BehaviorSubject<string>('');
  page$!: Observable<IPage<IClientMinDto>>;
  currentPage: number = 1;
  totalItens: number = 0;

  clientService = inject(ClientApiService);

  ngOnInit(): void {
    this.page$ = combineLatest([this.currentPageSubject, this.searchSubject]).pipe(
      switchMap(([page, searchTerm]) => {
        const apiPage = page - 1;
        return this.clientService.findAllPaged(apiPage, 10, searchTerm);
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
}
