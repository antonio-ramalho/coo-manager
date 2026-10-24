
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

export interface TopSellingProduct {
  productId: number;
  productName: string;
  totalQuantitySold: number;
  measureUnit: string;
}

export interface InventoryDashboard {
  totalStockValue: number;
  occupancy: StockOccupancy;
  expiringBatches: ExpiringBatch[];
  topSellingProducts: TopSellingProduct[];
}
