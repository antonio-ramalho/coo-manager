import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ExpiringBatch, TopSellingProduct } from '../../models/inventory-dashboard.model';

export interface FilterOption {
  value: number;
  label: string;
}

/**
 * Dumb Component para exibição dos widgets laterais de BI:
 * 1. Lotes vencendo com filtros por dias (3, 7, 30).
 * 2. Ranking dos mais vendidos com filtros por período (7, 30, 365 dias).
 */
@Component({
  selector: 'app-stock-sidebar-widgets',
  standalone: true,
  imports: [CommonModule, DatePipe],
  templateUrl: './stock-sidebar-widgets.component.html',
  styleUrls: ['./stock-sidebar-widgets.component.scss'],
})
export class StockSidebarWidgetsComponent {
  @Input() expiringBatches: ExpiringBatch[] = [];
  @Input() topSellingProducts: TopSellingProduct[] = [];
  @Input() selectedExpiringDays: number = 3;
  @Input() selectedTopSellingDays: number = 7;
  @Input() loadingExpiring: boolean = false;
  @Input() loadingTopSelling: boolean = false;

  @Output() expiringDaysChange = new EventEmitter<number>();
  @Output() topSellingDaysChange = new EventEmitter<number>();

  readonly expiringFilterOptions: FilterOption[] = [
    { value: 3, label: '3 Dias' },
    { value: 7, label: '7 Dias' },
    { value: 30, label: '30 Dias' },
  ];

  readonly topSellingFilterOptions: FilterOption[] = [
    { value: 7, label: '7 Dias' },
    { value: 30, label: '30 Dias' },
    { value: 365, label: 'último ano' },
  ];

  onSelectExpiringDays(days: number): void {
    if (this.selectedExpiringDays !== days) {
      this.expiringDaysChange.emit(days);
    }
  }

  onSelectTopSellingDays(days: number): void {
    if (this.selectedTopSellingDays !== days) {
      this.topSellingDaysChange.emit(days);
    }
  }
}
