import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { map, Observable } from 'rxjs';
import { ClientApiService } from '../../../clients/data/client-api.service';
import { InputBatchApiService } from '../../../inputs/data/input-batch-api.service';
import { ClientSummary } from '../../../clients/models/client.model';
import { InputBatchTableItem } from '../../../inputs/models/input-batch.model';
import { Status } from '../../../../shared/enums/status';
import { MeasureUnit } from '../../../../shared/enums/measure-unit';
import { ToastService } from '../../../../core/services/toast.service';
import { PageTitleComponent } from '../../../../shared/components/layout/page-title/page-title.component';
import { StepperComponent } from '../../../../shared/components/layout/stepper/stepper.component';
import { SearchableSelectComponent } from '../../../../shared/components/forms/searchable-select/searchable-select.component';
import { InputErrorComponent } from '../../../../shared/components/forms/input-error/input-error-component';
import { formatDocument } from '../../../../shared/utils/document-format';
import { buildSalesForm, buildSalesItemForm, SalesForm } from '../../forms/sales-form.builder';
import { SaleClientStepComponent } from '../../components/sale-client-step/sale-client-step.component';
import { SaleCartStepComponent } from '../../components/sale-cart-step/sale-cart-step.component';
import {
  SaleConfirmationDialogComponent,
  SaleConfirmationItem,
} from '../../components/sale-confirmation-dialog/sale-confirmation-dialog.component';
import { SalesApiService } from '../../data/sales-api.service';
import { SaleClientPreview, SalesInsertDto } from '../../models/sales-transaction.model';

/**
 * Smart Component para o fluxo transacional de Nova Venda.
 * Segue o padrão de formulários do sistema com PageTitleComponent, StepperComponent
 * e Dialog de confirmação antes de persistir no backend.
 */
@Component({
  selector: 'app-new-sale',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PageTitleComponent,
    StepperComponent,
    SearchableSelectComponent,
    InputErrorComponent,
    SaleClientStepComponent,
    SaleCartStepComponent,
    SaleConfirmationDialogComponent,
  ],
  templateUrl: './new-sale.component.html',
  styleUrl: './new-sale.component.scss',
})
export class NewSaleComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly clientApiService = inject(ClientApiService);
  private readonly batchApiService = inject(InputBatchApiService);
  private readonly salesApiService = inject(SalesApiService);
  private readonly toastService = inject(ToastService);
  private readonly router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

  readonly salesForm: FormGroup<SalesForm> = buildSalesForm(this.fb);
  readonly currentStep = signal<'client' | 'cart'>('client');
  readonly selectedClientPreview = signal<SaleClientPreview | null>(null);
  readonly isLoadingClientDetails = signal<boolean>(false);
  readonly totalSaleValue = signal<number>(0);

  readonly stepLabels: string[] = ['Identificação do Cliente', 'Carrinho de Insumos'];
  currentPage = 1;

  readonly isSubmitting = signal<boolean>(false);
  readonly isDialogOpen = signal<boolean>(false);

  confirmationClientName = '';
  confirmationClientDoc = '';
  confirmationItems: SaleConfirmationItem[] = [];

  get isCurrentStepInvalid(): boolean {
    if (this.currentPage === 1) {
      return !this.salesForm.controls.personId.valid;
    }
    return this.salesForm.invalid || this.salesForm.controls.items.length === 0;
  }

  get isSubmitDisabled(): boolean {
    return this.salesForm.invalid || this.salesForm.controls.items.length === 0;
  }

  ngOnInit(): void {
    this.setupRealTimeCalculations();
  }

  private setupRealTimeCalculations(): void {
    this.salesForm.controls.items.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        let grandTotal = 0;

        for (const itemGroup of this.salesForm.controls.items.controls) {
          const qty = itemGroup.get('quantity')?.value ?? 0;
          const unitPrice = itemGroup.get('unitPrice')?.value ?? 0;
          const itemTotal = Number((qty * unitPrice).toFixed(2));

          if (itemGroup.get('totalPrice')?.value !== itemTotal) {
            itemGroup.get('totalPrice')?.setValue(itemTotal, { emitEvent: false });
          }

          grandTotal += itemTotal;
        }

        this.totalSaleValue.set(Number(grandTotal.toFixed(2)));
      });
  }

  searchClientsFn = (term: string): Observable<ClientSummary[]> => {
    return this.clientApiService.findAllPaged(0, 20, term).pipe(
      map((page) => page.content.filter((c) => c.status === Status.ACTIVE)),
    );
  };

  searchBatchesFn = (term: string): Observable<InputBatchTableItem[]> => {
    return this.batchApiService.findPaged(0, 20, term, true).pipe(
      map((page) => page.content),
    );
  };

  clientLabelFormatter = (client: ClientSummary): string => {
    return `${client.legalName} (${formatDocument(client.documentNumber)})`;
  };

  onClientSelected(clientSummary: ClientSummary | null): void {
    if (!clientSummary) {
      this.selectedClientPreview.set(null);
      this.salesForm.controls.personId.setValue(null);
      return;
    }

    this.salesForm.controls.personId.setValue(clientSummary.id);
    this.isLoadingClientDetails.set(true);

    this.clientApiService
      .findById(clientSummary.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (fullClient) => {
          const birthDate =
            'birthDate' in fullClient
              ? fullClient.birthDate
              : 'foundationDate' in fullClient
                ? fullClient.foundationDate
                : undefined;

          this.selectedClientPreview.set({
            id: fullClient.id,
            legalName: fullClient.legalName,
            documentNumber: clientSummary.documentNumber,
            birthDate: birthDate,
            status: fullClient.status,
          });
          this.isLoadingClientDetails.set(false);
        },
        error: () => {
          this.isLoadingClientDetails.set(false);
        },
      });
  }

  onBatchSelected(batch: InputBatchTableItem | null): void {
    if (!batch) {
      return;
    }
    const existingIndex = this.salesForm.controls.items.controls.findIndex(
      (ctrl) => ctrl.get('batchId')?.value === batch.id,
    );

    if (existingIndex >= 0) {
      const existingCtrl = this.salesForm.controls.items.at(existingIndex);
      const currentQty = existingCtrl.get('quantity')?.value ?? 0;
      const availableStock = existingCtrl.get('availableStock')?.value ?? 0;
      const nextQty = Math.min(currentQty + 1, availableStock);
      existingCtrl.get('quantity')?.setValue(nextQty);
      return;
    }

    const itemForm = buildSalesItemForm(this.fb, {
      batchId: batch.id,
      productName: batch.productName,
      batchCode: batch.batchCode,
      availableStock: batch.currentQuantity,
      unitPrice: batch.unitPrice ?? 0,
      measureUnit: batch.measureUnit ?? MeasureUnit.UNIT,
      quantity: 1,
      totalPrice: batch.unitPrice ?? 0,
    });

    this.salesForm.controls.items.push(itemForm);
  }

  onRemoveItem(index: number): void {
    this.salesForm.controls.items.removeAt(index);
  }

  nextPage(): void {
    if (this.currentPage === 1) {
      if (!this.salesForm.controls.personId.valid) {
        this.salesForm.controls.personId.markAsTouched();
        return;
      }
      this.currentPage = 2;
      this.currentStep.set('cart');
    }
  }

  backPage(): void {
    if (this.currentPage > 1) {
      this.currentPage = 1;
      this.currentStep.set('client');
    }
  }

  goToStep(step: 'client' | 'cart'): void {
    if (step === 'cart' && !this.salesForm.controls.personId.valid) {
      this.salesForm.controls.personId.markAsTouched();
      return;
    }
    this.currentStep.set(step);
    this.currentPage = step === 'client' ? 1 : 2;
  }

  cancel(): void {
    void this.router.navigate(['/home/vendas']);
  }

  openConfirmationDialog(): void {
    if (this.salesForm.invalid || this.salesForm.controls.items.length === 0) {
      this.salesForm.markAllAsTouched();
      return;
    }

    const raw = this.salesForm.getRawValue();
    this.confirmationClientName = this.selectedClientPreview()?.legalName ?? 'Cliente selecionado';
    this.confirmationClientDoc = this.selectedClientPreview()?.documentNumber ?? '';
    this.confirmationItems = raw.items.map((item) => ({
      productName: item.productName ?? '',
      batchCode: item.batchCode ?? '',
      quantity: item.quantity ?? 0,
      measureUnit: item.measureUnit ?? MeasureUnit.UNIT,
      unitPrice: item.unitPrice ?? 0,
      totalPrice: item.totalPrice ?? 0,
    }));
    this.isDialogOpen.set(true);
  }

  closeConfirmationDialog(): void {
    this.isDialogOpen.set(false);
  }

  executeSaleSubmission(): void {
    if (this.salesForm.invalid || this.salesForm.controls.items.length === 0) {
      return;
    }

    this.isSubmitting.set(true);
    const rawValue = this.salesForm.getRawValue();
    const payload: SalesInsertDto = {
      farmerId: rawValue.personId!,
      items: rawValue.items.map((it) => ({
        batchId: it.batchId!,
        quantity: it.quantity!,
      })),
    };

    this.salesApiService
      .processSale(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.isSubmitting.set(false);
          this.isDialogOpen.set(false);
          this.toastService.showSuccess('Venda realizada com sucesso!');
          void this.router.navigate(['/home/vendas']);
        },
        error: (err: unknown) => {
          this.isSubmitting.set(false);
          this.isDialogOpen.set(false);
          const httpStatus = (err as { status?: number })?.status;
          if (httpStatus === undefined) {
            const errorMsg =
              (err as { error?: { message?: string; error?: string } })?.error?.message ||
              (err as { error?: { message?: string; error?: string } })?.error?.error ||
              'Erro inesperado ao finalizar venda.';
            this.toastService.showError(errorMsg);
          }
          console.error('Erro na submissão da venda:', err);
        },
      });
  }
}
