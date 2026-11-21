import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import {
  LucideCalendar,
  LucideFileText,
  LucideReceiptText,
  LucideUser,
  LucideX,
} from '@lucide/angular';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';
import { MeasureUnitAbbrPipe } from '../../../../shared/pipes/measure-unit-abbr.pipe';
import { ReceiptCodePipe } from '../../../../shared/pipes/receipt-code.pipe';
import { CpfCnpjPipe } from '../../../../shared/pipes/cpf-cnpj-pipe';
import { PaymentStatusPipe } from '../../../../shared/pipes/payment-status.pipe';
import { PaymentStatus, PAYMENT_STATUS_CLASSES } from '../../../../shared/enums/payment-status.enum';
import { SalesDto } from '../../models/sales-transaction.model';

/**
 * Dumb Component somente leitura para exibir os detalhes completos de uma venda,
 * incluindo status, dados do cliente e itens comercializados.
 */
@Component({
  selector: 'app-sale-details-dialog',
  standalone: true,
  imports: [
    CommonModule,
    CurrencyPipe,
    DatePipe,
    ButtonComponent,
    LoadingSpinnerComponent,
    MeasureUnitAbbrPipe,
    ReceiptCodePipe,
    CpfCnpjPipe,
    PaymentStatusPipe,
    LucideReceiptText,
    LucideUser,
    LucideFileText,
    LucideCalendar,
    LucideX,
  ],
  templateUrl: './sale-details-dialog.component.html',
  styleUrls: ['./sale-details-dialog.component.scss'],
})
export class SaleDetailsDialogComponent {
  @Input() isOpen = false;
  @Input() loading = false;
  @Input() sale: SalesDto | null = null;

  @Output() close = new EventEmitter<void>();

  getStatusBadgeClass(status: PaymentStatus | string): string {
    const normalized = (typeof status === 'string' ? status.toUpperCase() : status) as PaymentStatus;
    return PAYMENT_STATUS_CLASSES[normalized] || 'status-neutral';
  }

  onClose(): void {
    this.close.emit();
  }
}

