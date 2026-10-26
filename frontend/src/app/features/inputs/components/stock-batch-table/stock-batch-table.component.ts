import { Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ConnectedPosition, OverlayModule } from '@angular/cdk/overlay';
import { LucideEllipsis } from '@lucide/angular';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { SearchBarComponent } from '../../../../shared/components/lists/search-bar/search-bar-component';
import { PaginationComponent } from '../../../../shared/components/lists/generic-pagination/pagination-component';
import { GenericTableComponent } from '../../../../shared/components/tables/generic-table/generic-table.component';
import { TableColumn } from '../../../../shared/components/tables/generic-table/table-column.model';
import { InputBatchTableItem } from '../../models/input-batch.model';
import { BatchStatus } from '../../../../shared/enums/batch-status';

/**
 * Dumb Component que empacota a visualização da tabela densa de lotes de insumos,
 * definindo as colunas variáveis, ações de linha (menu de 3 pontinhos para editar/ativar/desativar)
 * e integrando com o GenericTableComponent, SearchBarComponent e o PaginationComponent reutilizável.
 */
@Component({
  selector: 'app-stock-batch-table',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    OverlayModule,
    LucideEllipsis,
    ButtonComponent,
    SearchBarComponent,
    PaginationComponent,
    GenericTableComponent,
  ],
  templateUrl: './stock-batch-table.component.html',
  styleUrls: ['./stock-batch-table.component.scss'],
})
export class StockBatchTableComponent implements OnInit {
  @Input() batches: InputBatchTableItem[] = [];
  @Input() loading: boolean = false;
  @Input() currentPage: number = 1;
  @Input() totalPages: number = 1;
  @Input() totalElements: number = 0;

  @Output() searchChange = new EventEmitter<string>();
  @Output() pageChange = new EventEmitter<number>();
  @Output() filterClick = new EventEmitter<void>();
  @Output() edit = new EventEmitter<number>();
  @Output() deactivate = new EventEmitter<number>();
  @Output() activate = new EventEmitter<number>();

  @ViewChild('actionsTemplate', { static: true }) actionsTemplate!: TemplateRef<any>;

  columns: TableColumn<InputBatchTableItem>[] = [];
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
      { key: 'productName', header: 'NOME', field: 'productName' },
      { key: 'batchCode', header: 'LOTE', field: 'batchCode' },
      { key: 'productCode', header: 'CÓDIGO', field: 'productCode' },
      { key: 'expirationDate', header: 'VALIDADE', field: 'expirationDate', type: 'date' },
      { key: 'currentQuantity', header: 'QUANTIDADE', field: 'currentQuantity' },
      {
        key: 'status',
        header: 'STATUS',
        field: 'statusDescription',
        type: 'badge',
        cellClass: (row) => this.getStatusClass(row.status),
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

  onEdit(id: number, event: Event): void {
    event.stopPropagation();
    this.openMenuRowId = null;
    this.edit.emit(id);
  }

  onDeactivate(id: number, event: Event): void {
    event.stopPropagation();
    this.openMenuRowId = null;
    this.deactivate.emit(id);
  }

  onActivate(id: number, event: Event): void {
    event.stopPropagation();
    this.openMenuRowId = null;
    this.activate.emit(id);
  }

  private getStatusClass(status: BatchStatus | string): string {
    switch (status) {
      case BatchStatus.IN_STOCK:
      case 'IN_STOCK':
        return 'status-badge-in-stock';
      case BatchStatus.LOW_STOCK:
      case 'LOW_STOCK':
        return 'status-badge-low-stock';
      case BatchStatus.OUT_OF_STOCK:
      case 'OUT_OF_STOCK':
      case BatchStatus.EXPIRED:
      case 'EXPIRED':
        return 'status-badge-out-of-stock';
      default:
        return 'status-badge-neutral';
    }
  }
}
