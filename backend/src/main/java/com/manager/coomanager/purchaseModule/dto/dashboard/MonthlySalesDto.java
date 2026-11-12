package com.manager.coomanager.purchaseModule.dto.dashboard;

/**
 * Representa o volume financeiro e quantidade de vendas consolidado por mês.
 */
public record MonthlySalesDto(
        int year,
        int month,
        String monthLabel,
        Double totalValue,
        Long salesCount
) {}
