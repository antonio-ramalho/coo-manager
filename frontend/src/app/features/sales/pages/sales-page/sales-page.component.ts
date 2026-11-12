import { Component, DestroyRef, inject, signal } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';
import { takeUntilDestroyed, toSignal } from '@angular/core/rxjs-interop';
import { BehaviorSubject, combineLatest, map, Observable, switchMap, catchError, of, tap } from 'rxjs';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { DesktopOnlyNoticeComponent } from '../../../../shared/components/feedback/desktop-only-notice/desktop-only-notice.component';
import { SalesTableComponent } from '../../components/sales-table/sales-table.component';
import { SalesSidebarWidgetsComponent } from '../../components/sales-sidebar-widgets/sales-sidebar-widgets.component';
import { SaleDetailsDialogComponent } from '../../components/sale-details-dialog/sale-details-dialog.component';
import { SalesApiService } from '../../data/sales-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { RecentSaleItem, SalesDto, SalesTableItem } from '../../models/sales-transaction.model';
import { Page } from '../../../../core/models/page.model';

/**
 * Smart Component para a listagem e controle de Vendas.
 * Orquestra chamadas à API, gerencia estado de busca, paginação e modal de detalhes
 * para os Dumb Components da tabela e widget lateral.
 */
@Component({
  selector: 'app-sales-page',
  standalone: true,
  imports: [
    CommonModule,
    AsyncPipe,
    RouterLink,
    ButtonComponent,
    DesktopOnlyNoticeComponent,
    SalesTableComponent,
    SalesSidebarWidgetsComponent,
    SaleDetailsDialogComponent,
  ],
  templateUrl: './sales-page.component.html',
  styleUrls: ['./sales-page.component.scss'],
})
export class SalesPageComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly salesService = inject(SalesApiService);
  private readonly toastService = inject(ToastService);
  private readonly destroyRef = inject(DestroyRef);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  readonly pageIndex$ = new BehaviorSubject<number>(0);
  readonly searchTerm$ = new BehaviorSubject<string>('');
  private readonly refreshTrigger$ = new BehaviorSubject<void>(undefined);

  loadingSales = false;
  loadingRecent = false;

  readonly isDetailsOpen = signal(false);
  readonly loadingDetails = signal(false);
  readonly selectedSaleDetails = signal<SalesDto | null>(null);

  readonly pagedSales$: Observable<Page<SalesTableItem>> = combineLatest([
    this.pageIndex$,
    this.searchTerm$,
    this.refreshTrigger$,
  ]).pipe(
    tap(() => (this.loadingSales = true)),
    switchMap(([page, term]) =>
      this.salesService.findPaged(page, 10, term).pipe(
        catchError((err) => {
          console.error('Erro ao buscar vendas paginadas:', err);
          return of({
            content: [],
            totalElements: 0,
            totalPages: 1,
            first: true,
            last: true,
            pageable: { pageNumber: 0, pageSize: 10 },
          });
        }),
      ),
    ),
    tap(() => (this.loadingSales = false)),
  );

  readonly recentSales$: Observable<RecentSaleItem[]> = this.refreshTrigger$.pipe(
    tap(() => (this.loadingRecent = true)),
    switchMap(() =>
      this.salesService.findRecent().pipe(
        catchError((err) => {
          console.error('Erro ao buscar vendas recentes:', err);
          return of([]);
        }),
      ),
    ),
    tap(() => (this.loadingRecent = false)),
  );

  onSearchChange(term: string): void {
    this.pageIndex$.next(0);
    this.searchTerm$.next(term);
  }

  onPageChange(page1Indexed: number): void {
    this.pageIndex$.next(page1Indexed - 1);
  }

  onViewDetails(id: number): void {
    this.selectedSaleDetails.set(null);
    this.loadingDetails.set(true);
    this.isDetailsOpen.set(true);

    this.salesService
      .getSaleById(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (sale) => {
          this.selectedSaleDetails.set(sale);
          this.loadingDetails.set(false);
        },
        error: (err) => {
          console.error('Erro ao buscar detalhes da venda:', err);
          this.loadingDetails.set(false);
          this.isDetailsOpen.set(false);
        },
      });
  }

  closeDetailsDialog(): void {
    this.isDetailsOpen.set(false);
    this.selectedSaleDetails.set(null);
  }

  onRegisterPayment(id: number): void {
    this.salesService
      .registerPayment(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.toastService.showSuccess('Pagamento registrado com sucesso.');
          this.refreshTrigger$.next();
        },
        error: (err: unknown) => {
          console.error(err);
        },
      });
  }

  onCancelSale(id: number): void {
    this.salesService
      .cancelSale(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.toastService.showSuccess('Venda cancelada com sucesso.');
          this.refreshTrigger$.next();
        },
        error: (err: unknown) => {
          console.error(err);
        },
      });
  }
}
