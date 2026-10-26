import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { LucideCalendarClock, LucideCalendarPlus, LucidePackageSearch } from '@lucide/angular';
import { MeasureUnitPipe } from '../../../pipes/measure-unit-pipe';
import { MeasureUnit } from '../../../enums/measure-unit';
import { BatchStatus } from '../../../enums/batch-status';
import { InputProductBatchSummary } from '../../../../features/inputs/models/input-product.model';

/**
 * Dumb Component que renderiza a relação detalhada de lotes e datas de validade de um produto.
 * Apresenta destaque para o próximo vencimento, última entrada e listagem compacta com scroll.
 */
@Component({
  selector: 'app-product-batches-card',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    DecimalPipe,
    MeasureUnitPipe,
    LucideCalendarClock,
    LucideCalendarPlus,
    LucidePackageSearch,
  ],
  templateUrl: './product-batches-info.html',
  styleUrls: ['./product-batches-info.scss'],
})
export class ProductBatchesCardComponent {
  @Input({ required: true }) batches: InputProductBatchSummary[] = [];
  @Input({ required: true }) measureUnit!: MeasureUnit | string;

  get hasBatches(): boolean {
    return !!this.batches && this.batches.length > 0;
  }

  get nextExpiringBatch(): InputProductBatchSummary | null {
    if (!this.hasBatches) return null;
    const active = this.batches.filter(
      (b) => b.status !== BatchStatus.INACTIVE && b.status !== 'INACTIVE' && b.currentQuantity > 0,
    );
    if (active.length === 0) return null;
    return [...active].sort(
      (a, b) => new Date(a.expirationDate).getTime() - new Date(b.expirationDate).getTime(),
    )[0];
  }

  get latestEntryBatch(): InputProductBatchSummary | null {
    if (!this.hasBatches) return null;
    return [...this.batches].sort(
      (a, b) => new Date(b.entryDate).getTime() - new Date(a.entryDate).getTime(),
    )[0];
  }

  getBatchStatusClass(status: BatchStatus | string): string {
    switch (status) {
      case BatchStatus.IN_STOCK:
      case 'IN_STOCK':
        return 'badge-success';
      case BatchStatus.LOW_STOCK:
      case 'LOW_STOCK':
        return 'badge-warning';
      case BatchStatus.OUT_OF_STOCK:
      case 'OUT_OF_STOCK':
      case BatchStatus.EXPIRED:
      case 'EXPIRED':
        return 'badge-danger';
      default:
        return 'badge-neutral';
    }
  }
}
