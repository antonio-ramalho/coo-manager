package com.manager.coopafi.dto.inputPurchase;

import com.manager.coopafi.dto.inputPurchaseItem.InputPurchaseItemInsertDto;

import java.util.List;

public record InputPurchaseInsertDto(
        Long farmerId,
        List<InputPurchaseItemInsertDto> items
) {}