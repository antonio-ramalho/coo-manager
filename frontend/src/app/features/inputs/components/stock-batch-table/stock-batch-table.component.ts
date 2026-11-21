import { Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { ButtonComponent } from '../../../../shared/components/ui/button/button-component';
import { SearchBarComponent } from '../../../../shared/components/lists/search-bar/search-bar-component';
import { PaginationComponent } from '../../../../shared/components/lists/generic-pagination/pagination-component';
import { GenericTableComponent } from '../../../../shared/components/tables/generic-table/generic-table.component';
import { TableColumn } from '../../../../shared/components/tables/generic-table/table-column.model';
import { ActionMenuComponent } from '../../../../shared/components/ui/action-menu/action-menu.component';
import { ActionMenuItem } from '../../../../shared/components/ui/action-menu/action-menu-item.model';
import { InputBatchTableItem } from '../../models/input-batch.model';
import { BatchStatus } from '../../../../shared/enums/batch-status';

/**
 * Dumb Component que empacota a visualização da tabela densa de lotes de insumos,
 * definindo as colunas variáveis, ações de linha via ActionMenuComponent
 * e integrando com o GenericTableComponent, SearchBarComponent e o PaginationComponent reutilizável.
 */
@Component({
  selector: 'app-stock-batch-table',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ButtonComponent,
    SearchBarComponent,
    PaginationComponent,
    GenericTableComponent,
    ActionMenuComponent,
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

  getBatchActions(row: InputBatchTableItem): ActionMenuItem[] {
    const actions: ActionMenuItem[] = [
      {
        label: 'Editar',
        action: () => this.edit.emit(row.id),
      },
    ];

    if (row.status !== 'INACTIVE') {
      actions.push({
        label: 'Desativar',
        danger: true,
        dividerAbove: true,
        action: () => this.deactivate.emit(row.id),
      });
    } else {
      actions.push({
        label: 'Ativar',
        success: true,
        dividerAbove: true,
        action: () => this.activate.emit(row.id),
      });
    }

    return actions;
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
