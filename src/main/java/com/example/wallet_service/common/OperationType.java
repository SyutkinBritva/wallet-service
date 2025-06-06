package com.example.wallet_service.common;

import java.util.Arrays;

import com.example.wallet_service.exception.WalletValidationException;

public enum OperationType {

    DEPOSIT,
    WITHDRAW;

    public static OperationType from(final String value) {

        return Arrays.stream(OperationType.values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new WalletValidationException("Unsupported operation type: " + value));
    }
}
