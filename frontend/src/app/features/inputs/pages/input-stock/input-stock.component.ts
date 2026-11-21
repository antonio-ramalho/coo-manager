import { Component, inject, signal } from '@angular/core';
import { CommonModule, AsyncPipe } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { BehaviorSubject, combineLatest, map, Observable, switchMap, catchError, of, tap } from 'rxjs';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { DesktopOnlyNoticeComponent } from '../../../../shared/components/feedback/desktop-only-notice/desktop-only-notice.component';
import { StockKpiCardsComponent } from '../../components/stock-kpi-cards/stock-kpi-cards.component';
import { StockBatchTableComponent } from '../../components/stock-batch-table/stock-batch-table.component';
import { StockSidebarWidgetsComponent } from '../../components/stock-sidebar-widgets/stock-sidebar-widgets.component';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';
import { BaseModalComponent } from '../../../../shared/components/feedback/base-modal/base-modal.component';
import { BatchFormComponent } from '../../../../shared/components/forms/batch-form/batch-form.component';
import { InventoryApiService } from '../../data/inventory-api.service';
import { InputBatchApiService } from '../../data/input-batch-api.service';
import { InputProductApiService } from '../../data/input-product-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { ExpiredBatch, ExpiringBatch, InventoryDashboard } from '../../models/inventory-dashboard.model';
import { InputBatchTableItem } from '../../models/input-batch.model';
import { InputProductSummary } from '../../models/input-product.model';
import { buildBatchForm } from '../../forms/batch-form.builder';
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
    ReactiveFormsModule,
    AsyncPipe,
    ButtonComponent,
    DesktopOnlyNoticeComponent,
    StockKpiCardsComponent,
    StockBatchTableComponent,
    StockSidebarWidgetsComponent,
    LoadingSpinnerComponent,
    BaseModalComponent,
    BatchFormComponent,
  ],
  templateUrl: './input-stock.component.html',
  styleUrls: ['./input-stock.component.scss'],
})
export class InputStockComponent {
  private readonly fb = inject(FormBuilder);
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly inventoryService = inject(InventoryApiService);
  private readonly batchService = inject(InputBatchApiService);
  private readonly productService = inject(InputProductApiService);
  private readonly toastService = inject(ToastService);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  readonly isBatchModalOpen = signal(false);
  readonly editingBatchId = signal<number | null>(null);
  readonly isSavingBatch = signal(false);
  readonly products = signal<InputProductSummary[]>([]);
  readonly batchForm: FormGroup = buildBatchForm(this.fb, true);

  readonly searchProductsFn = (term: string) => {
    return this.productService.findAllByPage(0, 10, term).pipe(
      map((page) => page.content.filter((p) => p.productCatalogStatus === 'ACTIVE' || !p.productCatalogStatus)),
      catchError(() => of([])),
    );
  };

  readonly expiringDays$ = new BehaviorSubject<number>(3);
  readonly expiredDays$ = new BehaviorSubject<number>(3);
  readonly pageIndex$ = new BehaviorSubject<number>(0);
  readonly searchTerm$ = new BehaviorSubject<string>('');
  private readonly refreshTrigger$ = new BehaviorSubject<void>(undefined);

  loadingBatches = false;
  loadingExpiring = false;
  loadingExpired = false;

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

        readonly expiredBatches$: Observable<ExpiredBatch[]> = combineLatest([
          this.expiredDays$,
          this.refreshTrigger$,
        ]).pipe(
          tap(() => (this.loadingExpired = true)),
          switchMap(([days]) =>
            this.inventoryService.getExpiredBatches(days).pipe(
              catchError((err) => {
                console.error('Erro ao buscar lotes vencidos:', err);
                return of([]);
              }),
            ),
          ),
          tap(() => (this.loadingExpired = false)),
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

    onExpiredDaysChange(days: number): void {
      this.expiredDays$.next(days);
    }

    onEditBatch(id: number): void {
      this.editingBatchId.set(id);
      this.batchForm.reset();
      this.isBatchModalOpen.set(true);
      this.loadActiveProducts();
      this.batchService.findById(id).subscribe({
        next: (batch) => {
          this.batchForm.patchValue({
            batchCode: batch.batchCode,
            inputProductId: batch.inputProductId,
            quantityValue: batch.currentQuantity,
            expirationDate: batch.expirationDate,
          });
        },
        error: (err: unknown) => {
          this.toastService.showError('Erro ao carregar dados do lote para edição.');
          console.error(err);
          this.closeBatchModal();
        },
      });
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

    openNewBatchModal(): void {
      this.editingBatchId.set(null);
      this.batchForm.reset();
      this.isBatchModalOpen.set(true);
      this.loadActiveProducts();
    }

    closeBatchModal(): void {
      this.isBatchModalOpen.set(false);
    }

    onSaveBatch(): void {
      if (this.batchForm.invalid) {
        this.batchForm.markAllAsTouched();
        this.toastService.showError('Preencha os campos obrigatórios do lote.');
        return;
      }

      this.isSavingBatch.set(true);
      const values = this.batchForm.getRawValue();
      const editingId = this.editingBatchId();

      const request$ = editingId
        ? this.batchService.update(editingId, {
            expirationDate: values.expirationDate,
          })
        : this.batchService.insert({
            batchCode: values.batchCode,
            inputProductId: values.inputProductId,
            quantityValue: Number(String(values.quantityValue).replace(',', '.')),
            expirationDate: values.expirationDate,
          });

      request$.subscribe({
        next: () => {
          this.isSavingBatch.set(false);
          this.isBatchModalOpen.set(false);
          this.toastService.showSuccess(
            editingId ? 'Lote atualizado com sucesso!' : 'Lote cadastrado com sucesso!',
          );
          this.refreshTrigger$.next();
        },
        error: (err: unknown) => {
          this.isSavingBatch.set(false);
          this.toastService.showError(
            editingId ? 'Erro ao atualizar lote.' : 'Erro ao cadastrar lote.',
          );
          console.error(err);
        },
      });
    }

    private loadActiveProducts(): void {
      this.productService.findAllByPage(0, 50).subscribe({
        next: (page) => {
          const active = page.content.filter(
            (p) => p.productCatalogStatus === 'ACTIVE' || !p.productCatalogStatus,
          );
          this.products.set(active);
        },
        error: (err) => {
          console.error('Erro ao carregar insumos para seleção:', err);
        },
      });
    }
  }
