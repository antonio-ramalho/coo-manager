
export interface InputBatchTableItem {
  id: number;
  batchCode: string;
  productName: string;
  productCode: string;
  expirationDate: string;
  currentQuantity: number;
  status: string;
  statusDescription: string;
}

export interface InputBatch {
  id: number;
  inputProductId: number;
  productName: string;
  originalQuantity: number;
  currentQuantity: number;
  entryDate: string;
  expirationDate: string;
  status: string;
}

export interface InputBatchInsert {
  inputProductId: number;
  quantityValue: number;
  expirationDate: string;
}

export interface InputBatchUpdate {
  expirationDate?: string;
}
