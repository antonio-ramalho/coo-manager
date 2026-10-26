
export interface SalesItemInsertDto {
  batchId: number;
  quantity: number;
}

export interface SalesInsertDto {
  farmerId: number;
  items: SalesItemInsertDto[];
}

export interface SalesItemDto {
  productName: string;
  quantity: number;
  unitPrice: number;
  itemTotalValue: number;
}

export interface SalesDto {
  id: number;
  farmerName: string;
  totalValue: number;
  purchaseDate: string;
  paymentStatus: string;
  items: SalesItemDto[];
}

export interface SaleCartItem {
  batchId: number;
  batchCode: string;
  productId: number;
  productName: string;
  availableStock: number;
  unitPrice: number;
  quantity: number;
  measureUnit: string;
  totalPrice: number;
}