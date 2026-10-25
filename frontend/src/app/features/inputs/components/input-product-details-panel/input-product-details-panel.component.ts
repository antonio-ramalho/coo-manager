import { Component, EventEmitter, Input, Output } from '@angular/core';
import { DetailsPanelComponent } from '../../../../shared/components/layout/details-panel/details-panel.component';
import { ProductDetailsHeaderComponent } from '../../../../shared/components/data-display/product-details-header/product-details-header';
import { ProductBasicInfoCardComponent } from '../../../../shared/components/data-display/product-info/product-info';
import { ProductStockSummaryCardComponent } from '../../../../shared/components/data-display/product-stock-summary/product-stock-summary';
import { ProductBatchesCardComponent } from '../../../../shared/components/data-display/product-batches-info/product-batches-info';
import { InputProduct } from '../../models/input-product.model';

/**
 * Dumb Component responsável por compor os painéis de detalhes de um produto de insumo.
 * Reutiliza os componentes genéricos de data-display: header, basic-info, stock-summary e batches-card.
 */
@Component({
  selector: 'app-input-product-details-panel',
  standalone: true,
  imports: [
    DetailsPanelComponent,
    ProductDetailsHeaderComponent,
    ProductBasicInfoCardComponent,
    ProductStockSummaryCardComponent,
    ProductBatchesCardComponent,
  ],
  templateUrl: './input-product-details-panel.component.html',
  styleUrls: ['./input-product-details-panel.component.scss'],
  host: {
    '[style.height]': '"100%"',
  },
})
export class InputProductDetailsPanelComponent {
  @Input({ required: true }) product!: InputProduct;
  @Output() edit = new EventEmitter<number>();
}
