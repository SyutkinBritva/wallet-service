package com.example.wallet_service.common;

import java.util.Arrays;

import com.example.wallet_service.exception.WalletValidationException;

/**
 * Перечисление типов операций с кошельком.
 * <ul>
 *     <li>{@code DEPOSIT} — пополнение баланса</li>
 *     <li>{@code WITHDRAW} — снятие средств</li>
 * </ul>
 */
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
