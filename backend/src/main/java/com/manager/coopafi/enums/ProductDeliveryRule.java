package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductDeliveryRule {
    VALUE_ONLY,
    QUANTITY_LOCKED,
    GROUP_LOCKED;
}
