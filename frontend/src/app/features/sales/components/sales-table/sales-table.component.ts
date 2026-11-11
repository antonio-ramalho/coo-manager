import { Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConnectedPosition, OverlayModule } from '@angular/cdk/overlay';
import { LucideEllipsis } from '@lucide/angular';
import { SearchBarComponent } from '../../../../shared/components/lists/search-bar/search-bar-component';
import { PaginationComponent } from '../../../../shared/components/lists/generic-pagination/pagination-component';
import { GenericTableComponent } from '../../../../shared/components/tables/generic-table/generic-table.component';
import { TableColumn } from '../../../../shared/components/tables/generic-table/table-column.model';
import { ReceiptCodePipe } from '../../../../shared/pipes/receipt-code.pipe';
import { SalesTableItem } from '../../models/sales-transaction.model';

/**
 * Dumb Component que empacota a visualização da tabela densa de vendas/saídas,
 * com busca multi-critério, paginação e menu de ações (incluindo visualização de detalhes).
 */
@Component({
  selector: 'app-sales-table',
  standalone: true,
  imports: [
    CommonModule,
    OverlayModule,
    LucideEllipsis,
    SearchBarComponent,
    PaginationComponent,
    GenericTableComponent,
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
  openMenuRowId: number | null = null;

  readonly overlayPositions: ConnectedPosition[] = [
    {
      originX: 'end',
      originY: 'bottom',
      overlayX: 'end',
      overlayY: 'top',
      offsetY: 4,
    },
    {
      originX: 'end',
      originY: 'top',
      overlayX: 'end',
      overlayY: 'bottom',
      offsetY: -4,
    },
  ];

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

  toggleMenu(id: number, event: Event): void {
    event.stopPropagation();
    this.openMenuRowId = this.openMenuRowId === id ? null : id;
  }

  onViewDetails(id: number, event: Event): void {
    event.stopPropagation();
    this.openMenuRowId = null;
    this.viewDetails.emit(id);
  }

  onPay(id: number, event: Event): void {
    event.stopPropagation();
    this.openMenuRowId = null;
    this.registerPayment.emit(id);
  }

  onCancel(id: number, event: Event): void {
    event.stopPropagation();
    this.openMenuRowId = null;
    this.cancelSale.emit(id);
  }

  private getStatusClass(status: string): string {
    switch (status) {
      case 'PAID':
        return 'badge-success';
      case 'PENDING':
        return 'badge-pending';
      case 'CANCELED':
        return 'badge-danger';
      default:
        return 'badge-neutral';
    }
  }
}
