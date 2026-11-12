import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormArray, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Observable } from 'rxjs';
import {
  LucidePackage,
  LucidePlus,
  LucideShoppingCart,
  LucideTrash2,
  LucideX,
} from '@lucide/angular';
import { SearchableSelectComponent } from '../../../../shared/components/forms/searchable-select/searchable-select.component';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { MeasureUnitAbbrPipe } from '../../../../shared/pipes/measure-unit-abbr.pipe';
import { InputBatchTableItem } from '../../../inputs/models/input-batch.model';
import { SalesForm, SalesItemForm } from '../../forms/sales-form.builder';

/**
 * Dumb Component responsável pela visão e edição dos itens do carrinho da venda,
 * permitindo inclusão inline via busca na tabela.
 */
@Component({
  selector: 'app-sale-cart-step',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    CurrencyPipe,
    SearchableSelectComponent,
    ButtonComponent,
    MeasureUnitAbbrPipe,
    LucideShoppingCart,
    LucidePackage,
    LucidePlus,
    LucideTrash2,
    LucideX,
  ],
  templateUrl: './sale-cart-step.component.html',
  styleUrl: './sale-cart-step.component.scss',
})
export class SaleCartStepComponent {
  @Input({ required: true }) formGroup!: FormGroup<SalesForm>;
  @Input({ required: true }) searchBatchesFn!: (term: string) => Observable<InputBatchTableItem[]>;

  @Output() batchSelected = new EventEmitter<InputBatchTableItem>();
  @Output() removeItem = new EventEmitter<number>();

  readonly isAddingItem = signal<boolean>(false);

  get itemsArray(): FormArray<FormGroup<SalesItemForm>> {
    return this.formGroup.controls.items;
  }

  batchLabelFormatter = (batch: InputBatchTableItem): string => {
    return `${batch.productName} (${batch.batchCode})`;
  };

  startAddingItem(): void {
    this.isAddingItem.set(true);
  }

  cancelAddingItem(): void {
    this.isAddingItem.set(false);
  }

  onBatchChosen(batch: InputBatchTableItem | null): void {
    if (!batch) {
      return;
    }
    this.batchSelected.emit(batch);
    this.isAddingItem.set(false);
  }

  onRemove(index: number): void {
    this.removeItem.emit(index);
  }
}
