package com.example.wallet_service.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.wallet_service.common.OperationType;
import com.example.wallet_service.entity.Wallet;
import com.example.wallet_service.exception.WalletValidationException;
import com.example.wallet_service.service.WalletValidator;
/**
 * Реализация валидатора для операций с кошельком.
 */
@Component
public class WalletValidatorImpl implements WalletValidator {

    /**
     * Проверка возможности выполнения операции с кошельком.
     * <ul>
     *     <li>Если кошелек не найден — выбрасывается исключение.</li>
     *     <li>Если операция — снятие средств и средств недостаточно — выбрасывается исключение.</li>
     * </ul>
     *
     * @param type тип операции (DEPOSIT или WITHDRAW)
     * @param amount сумма операции
     * @param wallet кошелек, с которым производится операция
     * @throws WalletValidationException если кошелёк не найден или недостаточно средств
     */
    @Override
    public void validateWalletProcess(OperationType type, BigDecimal amount, Wallet wallet) {

        if (wallet == null) {
            throw new WalletValidationException("Wallet not found");
        }

        if (type == OperationType.WITHDRAW) {
            BigDecimal balance = wallet.getBalance();

            if (balance.compareTo(amount) < 0) {
                throw new WalletValidationException("Insufficient balance");
            }
        }
    }
}