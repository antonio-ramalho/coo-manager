import { Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SearchBarComponent } from '../../../../shared/components/lists/search-bar/search-bar-component';
import { PaginationComponent } from '../../../../shared/components/lists/generic-pagination/pagination-component';
import { GenericTableComponent } from '../../../../shared/components/tables/generic-table/generic-table.component';
import { TableColumn } from '../../../../shared/components/tables/generic-table/table-column.model';
import { ActionMenuComponent } from '../../../../shared/components/ui/action-menu/action-menu.component';
import { ActionMenuItem } from '../../../../shared/components/ui/action-menu/action-menu-item.model';
import { ReceiptCodePipe } from '../../../../shared/pipes/receipt-code.pipe';
import { SalesTableItem } from '../../models/sales-transaction.model';
import { PaymentStatus } from '../../../../shared/enums/payment-status.enum';

/**
 * Dumb Component que empacota a visualização da tabela densa de vendas/saídas,
 * com busca multi-critério, paginação e menu de ações padronizado (ActionMenuComponent).
 */
@Component({
  selector: 'app-sales-table',
  standalone: true,
  imports: [
    CommonModule,
    SearchBarComponent,
    PaginationComponent,
    GenericTableComponent,
    ActionMenuComponent,
  ],
  templateUrl: './sales-table.component.html',
  styleUrls: ['./sales-table.component.scss'],
})
export class SalesTableComponent implements OnInit {
  private readonly receiptCodePipe = new ReceiptCodePipe();

  @Input() sales: SalesTableItem[] = [];
  @Input() loading: boolean = false;
  @Input() currentPage: number = 1;
  @Input() totalElements: number = 0;

  @Output() searchChange = new EventEmitter<string>();
  @Output() pageChange = new EventEmitter<number>();
  @Output() filterClick = new EventEmitter<void>();
  @Output() viewDetails = new EventEmitter<number>();
  @Output() registerPayment = new EventEmitter<number>();
  @Output() cancelSale = new EventEmitter<number>();

  @ViewChild('actionsTemplate', { static: true }) actionsTemplate!: TemplateRef<any>;

  columns: TableColumn<SalesTableItem>[] = [];

  ngOnInit(): void {
    this.columns = [
      { key: 'clientName', header: 'CLIENTE', field: 'clientName' },
      {
        key: 'uuid',
        header: 'CÓDIGO',
        formatter: (row) => this.receiptCodePipe.transform(row.uuid),
      },
      { key: 'purchaseDate', header: 'DATA', field: 'purchaseDate', type: 'date' },
      {
        key: 'totalValue',
        header: 'VALOR',
        field: 'totalValue',
        type: 'currency',
      },
      {
        key: 'status',
        header: 'STATUS',
        field: 'paymentStatusDescription',
        type: 'badge',
        cellClass: (row) => this.getStatusClass(row.paymentStatus),
      },
      {
        key: 'actions',
        header: '',
        width: '48px',
        align: 'center',
        cellTemplate: this.actionsTemplate,
      },
    ];
  }

  getRowActions(row: SalesTableItem): ActionMenuItem[] {
    const actions: ActionMenuItem[] = [
      {
        label: 'Ver Detalhes',
        action: () => this.viewDetails.emit(row.id),
      },
    ];

    if (row.paymentStatus === PaymentStatus.PENDING) {
      actions.push({
        label: 'Registrar Pagamento',
        action: () => this.registerPayment.emit(row.id),
      });
    }

    if (row.paymentStatus !== PaymentStatus.CANCELED) {
      actions.push({
        label: 'Cancelar Venda',
        danger: true,
        dividerAbove: true,
        action: () => this.cancelSale.emit(row.id),
      });
    }

    return actions;
  }

  private getStatusClass(status: PaymentStatus | string): string {
    switch (status) {
      case PaymentStatus.PAID:
        return 'badge-success';
      case PaymentStatus.PENDING:
        return 'badge-pending';
      case PaymentStatus.CANCELED:
      case PaymentStatus.COMPLETED:
        return 'badge-danger';
      default:
        return 'badge-neutral';
    }
  }
}
