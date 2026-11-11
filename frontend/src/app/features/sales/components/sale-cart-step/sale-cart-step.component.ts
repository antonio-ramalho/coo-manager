import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormArray, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import {
  LucidePackage,
  LucideShoppingCart,
  LucideTrash2,
} from '@lucide/angular';
import { SearchableSelectComponent } from '../../../../shared/components/forms/searchable-select/searchable-select.component';
import { InputBatchTableItem } from '../../../inputs/models/input-batch.model';
import { SalesForm, SalesItemForm } from '../../forms/sales-form.builder';

/**
 * Dumb Component para o Passo 2: Carrinho de Lotes / Itens da Venda.
 * A seleção é restrita a lotes específicos disponíveis em estoque com recálculo em tempo real.
 */
@Component({
  selector: 'app-sale-cart-step',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CurrencyPipe,
    SearchableSelectComponent,
    LucideShoppingCart,
    LucidePackage,
    LucideTrash2,
  ],
  templateUrl: './sale-cart-step.component.html',
  styleUrl: './sale-cart-step.component.scss',
})
export class SaleCartStepComponent {
  @Input({ required: true }) formGroup!: FormGroup<SalesForm>;
  @Input({ required: true }) searchBatchesFn!: (term: string) => Observable<InputBatchTableItem[]>;
  @Input({ required: true }) totalValue: number = 0;

  @Output() batchSelected = new EventEmitter<InputBatchTableItem>();
  @Output() removeItem = new EventEmitter<number>();
  @Output() backToClient = new EventEmitter<void>();
  @Output() finalizeSale = new EventEmitter<void>();

  get itemsArray(): FormArray<FormGroup<SalesItemForm>> {
    return this.formGroup.controls.items;
  }

  batchLabelFormatter = (batch: InputBatchTableItem): string => {
    const priceStr = (batch.unitPrice ?? 0).toLocaleString('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    });
    return `[${batch.batchCode}] ${batch.productName} — Disp: ${batch.currentQuantity} ${batch.measureUnit ?? ''} (${priceStr})`;
  };

  onBatchChosen(batch: InputBatchTableItem | null): void {
    if (batch) {
      this.batchSelected.emit(batch);
    }
  }

  onRemove(index: number): void {
    this.removeItem.emit(index);
  }

  onBack(): void {
    this.backToClient.emit();
  }

  onFinalize(): void {
    if (this.formGroup.valid && this.itemsArray.length > 0) {
      this.finalizeSale.emit();
    } else {
      this.formGroup.markAllAsTouched();
    }
  }
}
