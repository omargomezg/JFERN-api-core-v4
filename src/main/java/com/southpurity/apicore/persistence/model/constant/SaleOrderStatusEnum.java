package com.southpurity.apicore.persistence.model.constant;

import java.util.List;

public enum SaleOrderStatusEnum {
    OK,
    FAILED,
    APPROVED,
    APPROVED_PARTIAL,
    REJECTED,
    PENDING,
    PENDING_VALIDATION,
    REFUNDED,
    ERROR,
    TIMEOUT,
    UNKNOWN;

    public static List<SaleOrderStatusEnum> isPending() {
        return List.of(PENDING, PENDING_VALIDATION);
    }
}

