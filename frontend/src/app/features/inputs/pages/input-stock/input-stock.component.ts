import { Component, inject } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { BehaviorSubject, combineLatest, map, Observable, switchMap, catchError, of, tap } from 'rxjs';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { DesktopOnlyNoticeComponent } from '../../../../shared/components/feedback/desktop-only-notice/desktop-only-notice.component';
import { StockKpiCardsComponent } from '../../components/stock-kpi-cards/stock-kpi-cards.component';
import { StockBatchTableComponent } from '../../components/stock-batch-table/stock-batch-table.component';
import { StockSidebarWidgetsComponent } from '../../components/stock-sidebar-widgets/stock-sidebar-widgets.component';
import { InventoryApiService } from '../../data/inventory-api.service';
import { InputBatchApiService } from '../../data/input-batch-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { ExpiringBatch, InventoryDashboard, TopSellingProduct } from '../../models/inventory-dashboard.model';
import { InputBatchTableItem } from '../../models/input-batch.model';
import { Page } from '../../../../core/models/page.model';

/**
 * Smart Component para o Dashboard e Inventário de Estoque.
 * Orquestra as chamadas aos serviços de BI e Lotes, gerencia paginação,
 * busca e filtros de período, repassando o estado aos Dumb Components.
 */
@Component({
  selector: 'app-input-stock',
  standalone: true,
  imports: [
    CommonModule,
    AsyncPipe,
    RouterLink,
    ButtonComponent,
    DesktopOnlyNoticeComponent,
    StockKpiCardsComponent,
    StockBatchTableComponent,
    StockSidebarWidgetsComponent,
  ],
  templateUrl: './input-stock.component.html',
  styleUrls: ['./input-stock.component.scss'],
})
export class InputStockComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly inventoryService = inject(InventoryApiService);
  private readonly batchService = inject(InputBatchApiService);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  readonly expiringDays$ = new BehaviorSubject<number>(3);
  readonly topSellingDays$ = new BehaviorSubject<number>(7);
  readonly pageIndex$ = new BehaviorSubject<number>(0);
  readonly searchTerm$ = new BehaviorSubject<string>('');
  private readonly refreshTrigger$ = new BehaviorSubject<void>(undefined);

  loadingBatches = false;
  loadingExpiring = false;
  loadingTopSelling = false;

  readonly summary$: Observable<InventoryDashboard | null> = this.refreshTrigger$.pipe(
    switchMap(() =>
      this.inventoryService.getDashboardSummary().pipe(
        catchError((err) => {
          console.error('Erro ao buscar resumo do dashboard:', err);
          return of(null);
        }),
      ),
    ),
  );

  readonly expiringBatches$: Observable<ExpiringBatch[]> = combineLatest([
    this.expiringDays$,
    this.refreshTrigger$,
  ]).pipe(
    tap(() => (this.loadingExpiring = true)),
    switchMap(([days]) =>
      this.inventoryService.getExpiringBatches(days).pipe(
        catchError((err) => {
          console.error('Erro ao buscar lotes a vencer:', err);
          return of([]);
        }),
      ),
    ),
    tap(() => (this.loadingExpiring = false)),
  );

  readonly topSellingProducts$: Observable<TopSellingProduct[]> = this.topSellingDays$.pipe(
    tap(() => (this.loadingTopSelling = true)),
    switchMap((days) =>
      this.inventoryService.getTopSellingProducts(days).pipe(
        catchError((err) => {
          console.error('Erro ao buscar ranking de produtos:', err);
          return of([]);
        }),
      ),
    ),
    tap(() => (this.loadingTopSelling = false)),
  );

  readonly pagedBatches$: Observable<Page<InputBatchTableItem>> = combineLatest([
    this.pageIndex$,
    this.searchTerm$,
    this.refreshTrigger$,
  ]).pipe(
    tap(() => (this.loadingBatches = true)),
    switchMap(([page, term]) =>
      this.batchService.findPaged(page, 10, term).pipe(
        catchError((err) => {
          console.error('Erro ao buscar lotes paginados:', err);
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
    tap(() => (this.loadingBatches = false)),
  );

  onSearchChange(term: string): void {
    this.pageIndex$.next(0);
    this.searchTerm$.next(term);
  }

  onPageChange(page1Indexed: number): void {
    this.pageIndex$.next(page1Indexed - 1);
  }

  onExpiringDaysChange(days: number): void {
    this.expiringDays$.next(days);
  }

  onTopSellingDaysChange(days: number): void {
    this.topSellingDays$.next(days);
  }

  onEditBatch(id: number): void {
    void this.router.navigate(['/home/insumos/lotes/editar', id]);
  }

  onDeactivateBatch(id: number): void {
    this.batchService.delete(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Lote desativado com sucesso.');
        this.refreshTrigger$.next();
      },
      error: (err: unknown) => {
        this.toastService.showError('Erro ao desativar o lote.');
        console.error(err);
      },
    });
  }

  onActivateBatch(id: number): void {
    this.batchService.activate(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Lote ativado com sucesso.');
        this.refreshTrigger$.next();
      },
      error: (err: unknown) => {
        this.toastService.showError('Erro ao ativar o lote.');
        console.error(err);
      },
    });
  }
}
