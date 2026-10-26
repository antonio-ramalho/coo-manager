import { Component, Input } from '@angular/core';
import { CommonModule, CurrencyPipe, DecimalPipe } from '@angular/common';
import { LucideBoxes, LucideCircleDollarSign, LucideLayers, LucideShieldCheck } from '@lucide/angular';
import { MeasureUnitPipe } from '../../../pipes/measure-unit-pipe';
import { MeasureUnit } from '../../../enums/measure-unit';
import { BatchStatus } from '../../../enums/batch-status';
import { InputProductBatchSummary } from '../../../../features/inputs/models/input-product.model';

/**
 * Dumb Component que renderiza a visão consolidada de estoque de um produto no catálogo.
 * Calcula o saldo disponível, valor total imobilizado e relação de lotes ativos.
 */
@Component({
  selector: 'app-product-stock-summary-card',
  standalone: true,
  imports: [
    CommonModule,
    CurrencyPipe,
    DecimalPipe,
    MeasureUnitPipe,
    LucideBoxes,
    LucideCircleDollarSign,
    LucideLayers,
    LucideShieldCheck,
  ],
  templateUrl: './product-stock-summary.html',
  styleUrls: ['./product-stock-summary.scss'],
})
export class ProductStockSummaryCardComponent {
  @Input({ required: true }) batches: InputProductBatchSummary[] = [];
  @Input({ required: true }) unitPrice: number = 0;
  @Input({ required: true }) measureUnit!: MeasureUnit | string;

  get totalAvailableQuantity(): number {
    if (!this.batches || this.batches.length === 0) return 0;
    return this.batches
      .filter((b) => b.status !== BatchStatus.INACTIVE && b.status !== BatchStatus.EXPIRED && b.status !== 'INACTIVE' && b.status !== 'EXPIRED')
      .reduce((acc, curr) => acc + (curr.currentQuantity || 0), 0);
  }

  get totalStockFinancialValue(): number {
    return this.totalAvailableQuantity * (this.unitPrice || 0);
  }

  get activeBatchesCount(): number {
    if (!this.batches) return 0;
    return this.batches.filter((b) => b.status !== BatchStatus.INACTIVE && b.status !== 'INACTIVE').length;
  }

  get totalBatchesCount(): number {
    return this.batches ? this.batches.length : 0;
  }

  get statusLabel(): string {
    if (this.totalBatchesCount === 0) return 'Sem Lotes';
    if (this.totalAvailableQuantity <= 0) return 'Esgotado';
    const hasLowStock = this.batches.some(
      (b) => (b.status === BatchStatus.LOW_STOCK || b.status === 'LOW_STOCK') && b.currentQuantity > 0,
    );
    if (hasLowStock) return 'Estoque Baixo';
    return 'Em Estoque';
  }

  get statusClass(): string {
    if (this.totalBatchesCount === 0) return 'badge-neutral';
    if (this.totalAvailableQuantity <= 0) return 'badge-danger';
    const hasLowStock = this.batches.some(
      (b) => (b.status === BatchStatus.LOW_STOCK || b.status === 'LOW_STOCK') && b.currentQuantity > 0,
    );
    if (hasLowStock) return 'badge-warning';
    return 'badge-success';
  }
}
