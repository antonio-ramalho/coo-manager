import { Component, inject } from '@angular/core';
import { AsyncPipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { BreakpointObserver } from '@angular/cdk/layout';
import { toSignal } from '@angular/core/rxjs-interop';
import { BehaviorSubject, combineLatest, filter, map, Observable, switchMap } from 'rxjs';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { GenericListComponent } from '../../../../shared/components/lists/generic-list/generic-list-component';
import { DesktopOnlyNoticeComponent } from '../../../../shared/components/feedback/desktop-only-notice/desktop-only-notice.component';
import { InputProductDetailsPanelComponent } from '../../components/input-product-details-panel/input-product-details-panel.component';
import { ListCardItem } from '../../../../shared/models/list-card-item.model';
import { InputProductApiService } from '../../data/input-product-api.service';
import { ToastService } from '../../../../core/services/toast.service';
import { InputProduct } from '../../models/input-product.model';
import { toListCardItem } from '../../mappers/input-product.mapper';

/**
 * Smart Component para o Catálogo de Insumos.
 * Fornece visualização mestre-detalhe com degradação graciosa em telas menores via DesktopOnlyNotice.
 */
@Component({
  selector: 'app-input-catalog',
  standalone: true,
  imports: [
    GenericListComponent,
    ButtonComponent,
    AsyncPipe,
    RouterLink,
    DesktopOnlyNoticeComponent,
    InputProductDetailsPanelComponent,
  ],
  templateUrl: './input-catalog.component.html',
  styleUrls: ['./input-catalog.component.scss'],
})
export class InputCatalogComponent {
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly productService = inject(InputProductApiService);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);

  readonly isDesktop = toSignal(
    this.breakpointObserver.observe('(min-width: 768px)').pipe(map((result) => result.matches)),
    { initialValue: true },
  );

  private readonly currentPageSubject = new BehaviorSubject<number>(1);
  private readonly searchSubject = new BehaviorSubject<string>('');
  private readonly selectedProductIdSubject = new BehaviorSubject<number | null>(null);

  currentPage = 1;

  readonly page$: Observable<{ content: ListCardItem[]; totalElements: number }> = combineLatest([
    this.currentPageSubject,
    this.searchSubject,
  ]).pipe(
    switchMap(([page, searchTerm]) => {
      return this.productService.findAllByPage(page - 1, 10, searchTerm);
    }),
    map((response) => ({
      content: response.content.map(toListCardItem),
      totalElements: response.totalElements,
    })),
  );

  readonly productDetails$: Observable<InputProduct> = this.selectedProductIdSubject.pipe(
    filter((id): id is number => id !== null),
    switchMap((id) => this.productService.findById(id)),
  );

  applyFilters(searchTerm: string): void {
    const cleanTerm = searchTerm ? searchTerm.trim() : '';
    this.currentPage = 1;
    this.currentPageSubject.next(1);
    this.searchSubject.next(cleanTerm);
  }

  swappedPage(newPage: number): void {
    this.currentPage = newPage;
    this.currentPageSubject.next(newPage);
  }

  viewDetails(id: number): void {
    this.selectedProductIdSubject.next(id);
  }

  editProduct(id: number): void {
    void this.router.navigate(['/home/insumos/editar', id]);
  }

  deactivateProduct(id: number): void {
    this.productService.delete(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Insumo desativado com sucesso.');
        this.currentPageSubject.next(this.currentPage);
        if (this.selectedProductIdSubject.value === id) {
          this.selectedProductIdSubject.next(id);
        }
      },
      error: (error: unknown) => {
        this.toastService.showError('Erro ao desativar o insumo.');
        console.error(error);
      },
    });
  }

  activateProduct(id: number): void {
    this.productService.activate(id).subscribe({
      next: () => {
        this.toastService.showSuccess('Insumo ativado com sucesso.');
        this.currentPageSubject.next(this.currentPage);
        if (this.selectedProductIdSubject.value === id) {
          this.selectedProductIdSubject.next(id);
        }
      },
      error: (error: unknown) => {
        this.toastService.showError('Erro ao ativar o insumo.');
        console.error(error);
      },
    });
  }
}
