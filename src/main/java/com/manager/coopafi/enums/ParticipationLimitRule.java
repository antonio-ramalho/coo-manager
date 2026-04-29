package com.manager.coopafi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ParticipationLimitRule {
    PER_CPF,
    PER_CAF,
    UNLIMITED;
}


