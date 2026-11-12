export interface StockOccupancy {
  totalProducts: number;
  inStockCount: number;
  lowStockCount: number;
  outOfStockCount: number;
}

export interface ExpiringBatch {
  batchId: number;
  productName: string;
  expirationDate: string;
  daysRemaining: number;
  currentQuantity: number;
}

export interface ExpiredBatch {
  batchId: number;
  productName: string;
  expirationDate: string;
  daysExpired: number;
  currentQuantity: number;
}

export interface InventoryDashboard {
  totalStockValue: number;
  occupancy: StockOccupancy;
  expiringBatches: ExpiringBatch[];
}
