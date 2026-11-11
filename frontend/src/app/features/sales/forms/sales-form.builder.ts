import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { SaleCartItem } from '../models/sales-transaction.model';

export interface SalesItemForm {
  batchId: FormControl<number | null>;
  productName: FormControl<string>;
  batchCode: FormControl<string>;
  quantity: FormControl<number | null>;
  availableStock: FormControl<number>;
  unitPrice: FormControl<number>;
  measureUnit: FormControl<string>;
  totalPrice: FormControl<number>;
}

export interface SalesForm {
  personId: FormControl<number | null>;
  items: FormArray<FormGroup<SalesItemForm>>;
}

/**
 * Cria o formulário reativo para um item de venda (lote selecionado).
 */
export function buildSalesItemForm(
  fb: FormBuilder,
  item?: Partial<SaleCartItem>,
): FormGroup<SalesItemForm> {
  const quantity = item?.quantity ?? 1;
  const unitPrice = item?.unitPrice ?? 0;
  const availableStock = item?.availableStock ?? 0;

  return fb.group<SalesItemForm>({
    batchId: fb.control<number | null>(item?.batchId ?? null, [Validators.required]),
    productName: fb.control<string>(item?.productName ?? '', { nonNullable: true }),
    batchCode: fb.control<string>(item?.batchCode ?? '', { nonNullable: true }),
    quantity: fb.control<number | null>(quantity, [
      Validators.required,
      Validators.min(0.001),
      Validators.max(availableStock > 0 ? availableStock : 999999),
    ]),
    availableStock: fb.control<number>(availableStock, { nonNullable: true }),
    unitPrice: fb.control<number>(unitPrice, { nonNullable: true }),
    measureUnit: fb.control<string>(item?.measureUnit ?? 'UN', { nonNullable: true }),
    totalPrice: fb.control<number>(quantity * unitPrice, { nonNullable: true }),
  });
}

/**
 * Factory function isolada para a construção do formulário principal de Venda/Saída.
 */
export function buildSalesForm(fb: FormBuilder): FormGroup<SalesForm> {
  return fb.group<SalesForm>({
    personId: fb.control<number | null>(null, [Validators.required]),
    items: fb.array<FormGroup<SalesItemForm>>([], [Validators.required, Validators.minLength(1)]),
  });
}
