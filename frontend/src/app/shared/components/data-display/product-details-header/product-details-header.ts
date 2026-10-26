import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ButtonComponent } from '../../ui/button/button-component';
import { LucidePackage, LucideSquarePen } from '@lucide/angular';
import { ProductCatalogStatus } from '../../../enums/product-catalog-status';

@Component({
  selector: 'app-product-details-header',
  standalone: true,
  imports: [ButtonComponent, LucidePackage, LucideSquarePen],
  templateUrl: './product-details-header.html',
  styleUrls: ['./product-details-header.scss'],
})
export class ProductDetailsHeaderComponent {
  readonly ProductCatalogStatus = ProductCatalogStatus;

  @Input({ required: true }) id!: number;
  @Input({ required: true }) name!: string;
  @Input({ required: true }) code!: string;
  @Input({ required: true }) status!: ProductCatalogStatus | string;

  @Output() editProduct = new EventEmitter<number>();

  onEditProduct(event: Event): void {
    event.stopPropagation();
    this.editProduct.emit(this.id);
  }
}
