package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CultivationType {
    ORGANIC("Orgânico"),
    CONVENTIONAL("Convencional");

    private final String description;
}
