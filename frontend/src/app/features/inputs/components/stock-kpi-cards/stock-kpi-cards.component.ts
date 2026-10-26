import { Component, Input } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { LucideCircleDollarSign } from '@lucide/angular';
import { StockOccupancy } from '../../models/inventory-dashboard.model';

/**
 * Dumb Component para exibição dos KPIs analíticos do estoque:
 * Valor total monetário em estoque e barra segmentada de ocupação por status.
 */
@Component({
  selector: 'app-stock-kpi-cards',
  standalone: true,
  imports: [CurrencyPipe, LucideCircleDollarSign],
  templateUrl: './stock-kpi-cards.component.html',
  styleUrls: ['./stock-kpi-cards.component.scss'],
})
export class StockKpiCardsComponent {
  @Input() totalStockValue: number = 0;
  @Input() occupancy: StockOccupancy | null = null;

  get totalProducts(): number {
    return this.occupancy?.totalProducts ?? 0;
  }

  get inStockPercent(): number {
    if (!this.totalProducts) return 0;
    return ((this.occupancy?.inStockCount ?? 0) / this.totalProducts) * 100;
  }

  get lowStockPercent(): number {
    if (!this.totalProducts) return 0;
    return ((this.occupancy?.lowStockCount ?? 0) / this.totalProducts) * 100;
  }

  get outOfStockPercent(): number {
    if (!this.totalProducts) return 0;
    return ((this.occupancy?.outOfStockCount ?? 0) / this.totalProducts) * 100;
  }
}
