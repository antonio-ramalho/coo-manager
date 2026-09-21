package com.manager.coopafi.purchaseModule.dto.inputPurchase;

import com.manager.coopafi.purchaseModule.dto.inputPurchaseItem.InputPurchaseItemInsertDto;

import java.util.List;

public record InputPurchaseInsertDto(
        Long farmerId,
        List<InputPurchaseItemInsertDto> items
) {}