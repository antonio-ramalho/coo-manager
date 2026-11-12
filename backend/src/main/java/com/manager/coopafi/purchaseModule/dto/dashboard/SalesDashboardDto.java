package com.manager.coopafi.purchaseModule.dto.dashboard;

import java.util.List;

/**
 * DTO mestre consolidado com métricas gerenciais e de BI para o módulo de vendas.
 */
public record SalesDashboardDto(
        Double averageTicket,
        Double expirationRiskRate,
        List<MonthlySalesDto> monthlySales,
        List<TopSellingProductDto> topSellingProducts
) {}
