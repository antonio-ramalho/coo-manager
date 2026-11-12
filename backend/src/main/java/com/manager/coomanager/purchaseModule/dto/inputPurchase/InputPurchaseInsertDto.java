package com.manager.coomanager.purchaseModule.dto.inputPurchase;

import com.manager.coomanager.purchaseModule.dto.inputPurchaseItem.InputPurchaseItemInsertDto;

import java.util.List;

public record InputPurchaseInsertDto(
        Long farmerId,
        List<InputPurchaseItemInsertDto> items
) {}