import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { LucideCheckCircle, LucideX } from '@lucide/angular';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';

export interface SaleConfirmationItem {
  productName: string;
  batchCode: string;
  quantity: number;
  measureUnit: string;
  unitPrice: number;
  totalPrice: number;
}

/**
 * Dumb Component que renderiza a caixa de diálogo modal de confirmação
 * de venda/saída antes de enviar a requisição de finalização ao backend.
 */
@Component({
  selector: 'app-sale-confirmation-dialog',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, ButtonComponent, LucideCheckCircle, LucideX],
  templateUrl: './sale-confirmation-dialog.component.html',
  styleUrls: ['./sale-confirmation-dialog.component.scss'],
})
export class SaleConfirmationDialogComponent {
  @Input() isOpen = false;
  @Input() clientName = '';
  @Input() clientDocument = '';
  @Input() items: SaleConfirmationItem[] = [];
  @Input() totalValue = 0;
  @Input() isSubmitting = false;

  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onConfirm(): void {
    if (!this.isSubmitting) {
      this.confirm.emit();
    }
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
