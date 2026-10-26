import { BatchStatus } from '../../../shared/enums/batch-status';

export interface InputBatchTableItem {
  id: number;
  batchCode: string;
  productName: string;
  productCode: string;
  expirationDate: string;
  currentQuantity: number;
  status: BatchStatus | string;
  statusDescription: string;
}

export interface InputBatch {
  id: number;
  batchCode?: string;
  inputProductId: number;
  productName: string;
  originalQuantity: number;
  currentQuantity: number;
  entryDate: string;
  expirationDate: string;
  status: BatchStatus | string;
}

export interface InputBatchInsert {
  batchCode?: string;
  inputProductId: number;
  quantityValue: number;
  expirationDate: string;
}

export interface InputBatchUpdate {
  expirationDate?: string;
}
