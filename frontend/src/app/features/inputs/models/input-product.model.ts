import { MeasureUnit } from '../../../shared/enums/measure-unit';
import { ProductCatalogStatus } from '../../../shared/enums/product-catalog-status';
import { BatchStatus } from '../../../shared/enums/batch-status';

export interface InputProductBatchSummary {
  id: number;
  batchCode: string;
  currentQuantity: number;
  originalQuantity: number;
  entryDate: string;
  expirationDate: string;
  status: BatchStatus | string;
  statusDescription: string;
}

export interface InputProduct {
  id: number;
  productName: string;
  productCode: string;
  measureUnit: MeasureUnit | string;
  ncm: string;
  price: {
    value: number;
  };
  productCatalogStatus: ProductCatalogStatus | string;
  batches?: InputProductBatchSummary[];
}

export interface InputProductSummary {
  id: number;
  productName: string;
  productCode: string;
  productCatalogStatus: ProductCatalogStatus | string;
}

export interface InitialBatchFormPayload {
  quantityValue: number;
  expirationDate: string;
}

export interface InputProductInsert {
  productName: string;
  productCode: string;
  measureUnit: string;
  ncmValue: string;
  priceValue: number;
  initialBatch?: InitialBatchFormPayload;
}

export interface InputProductUpdate {
  productName?: string;
  priceValue?: number;
}