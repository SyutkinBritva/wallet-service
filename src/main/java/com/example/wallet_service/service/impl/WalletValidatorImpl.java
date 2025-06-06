package com.example.wallet_service.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.wallet_service.common.OperationType;
import com.example.wallet_service.entity.Wallet;
import com.example.wallet_service.exception.WalletValidationException;
import com.example.wallet_service.service.WalletValidator;

@Component
public class WalletValidatorImpl implements WalletValidator {

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