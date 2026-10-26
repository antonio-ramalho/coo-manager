import { ListCardItem } from '../../../shared/models/list-card-item.model';
import { ProductCatalogStatus } from '../../../shared/enums/product-catalog-status';
import { InputProduct, InputProductSummary } from '../models/input-product.model';

export function toListCardItem(product: InputProductSummary | InputProduct): ListCardItem {
  return {
    id: product.id,
    title: product.productName,
    subtitle: product.productCode ? `Cód: ${product.productCode}` : `Item #${product.id}`,
    isActive: product.productCatalogStatus === ProductCatalogStatus.ACTIVE,
  };
}
