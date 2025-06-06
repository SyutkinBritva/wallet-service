package com.example.wallet_service.service;

import java.math.BigDecimal;

import com.example.wallet_service.common.OperationType;
import com.example.wallet_service.entity.Wallet;

public interface WalletValidator {

    void validateWalletProcess(OperationType type, BigDecimal amount, Wallet wallet);
}
