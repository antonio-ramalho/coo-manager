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
  batchCode: string;
  measureUnit: string;
  quantity: number;
  unitPrice: number;
  itemTotalValue: number;
}

export interface SalesDto {
  id: number;
  uuid: string;
  clientName: string;
  clientDocument?: string | null;
  clientBirthDate?: string | null;
  clientStatus: string;
  totalValue: number;
  purchaseDate: string;
  paymentStatus: string;
  paymentStatusDescription: string;
  items: SalesItemDto[];
}

export interface SalesTableItem {
  id: number;
  uuid: string;
  clientName: string;
  purchaseDate: string;
  totalValue: number;
  paymentStatus: string;
  paymentStatusDescription: string;
}

export interface RecentSaleItem {
  id: number;
  productName: string;
  purchaseDate: string;
  totalValue: number;
}

export interface SaleClientPreview {
  id: number;
  legalName: string;
  documentNumber: string;
  birthDate?: string;
  status: string;
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
