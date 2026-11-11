import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ExpiredBatch, ExpiringBatch } from '../../models/inventory-dashboard.model';
import { LoadingSpinnerComponent } from '../../../../shared/components/ui/loading-spinner/loading-spinner.component';

export interface FilterOption {
  value: number;
  label: string;
}

/**
 * Dumb Component para exibição dos widgets laterais de BI do inventário:
 * 1. Lotes vencendo com filtros por dias (3, 7, 30).
 * 2. Lotes vencidos com filtros por dias (3, 7, 30).
 */
@Component({
  selector: 'app-stock-sidebar-widgets',
  standalone: true,
  imports: [CommonModule, DatePipe, LoadingSpinnerComponent],
  templateUrl: './stock-sidebar-widgets.component.html',
  styleUrls: ['./stock-sidebar-widgets.component.scss'],
})
export class StockSidebarWidgetsComponent {
  @Input() expiringBatches: ExpiringBatch[] = [];
  @Input() expiredBatches: ExpiredBatch[] = [];
  @Input() selectedExpiringDays: number = 3;
  @Input() selectedExpiredDays: number = 3;
  @Input() loadingExpiring: boolean = false;
  @Input() loadingExpired: boolean = false;

  @Output() expiringDaysChange = new EventEmitter<number>();
  @Output() expiredDaysChange = new EventEmitter<number>();

    readonly periodFilterOptions: FilterOption[] = [
      { value: 3, label: '3 Dias' },
      { value: 7, label: '7 Dias' },
      { value: 30, label: '30 Dias' },
    ];

    onSelectExpiringDays(days: number): void {
      if(this.selectedExpiringDays !== days) {
      this.expiringDaysChange.emit(days);
    }
  }

  onSelectExpiredDays(days: number): void {
    if(this.selectedExpiredDays !== days) {
    this.expiredDaysChange.emit(days);
  }
}
}
