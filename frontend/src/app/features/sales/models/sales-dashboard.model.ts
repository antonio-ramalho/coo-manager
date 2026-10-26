export interface MonthlySalesDto {
  year: number;
  month: number;
  monthLabel: string;
  totalValue: number;
  salesCount: number;
}

export interface TopSellingProductDto {
  productId: number;
  productName: string;
  totalQuantitySold: number;
  measureUnit: string;
}

export interface SalesDashboardDto {
  averageTicket: number;
  expirationRiskRate: number;
  monthlySales: MonthlySalesDto[];
  topSellingProducts: TopSellingProductDto[];
}
