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
import { MeasureUnitPipe } from '../../../../shared/pipes/measure-unit-pipe';
import { ReceiptCodePipe } from '../../../../shared/pipes/receipt-code.pipe';
import { formatDocument } from '../../../../shared/utils/document-format';
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
    MeasureUnitPipe,
    ReceiptCodePipe,
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

  formatDoc(doc: string | null | undefined): string {
    return doc ? formatDocument(doc) : 'Não informado';
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'PAID':
        return 'status-paid';
      case 'PENDING':
        return 'status-pending';
      case 'CANCELED':
        return 'status-canceled';
      default:
        return 'status-neutral';
    }
  }

  onClose(): void {
    this.close.emit();
  }
}

