package com.example.wallet_service;

import com.example.wallet_service.common.OperationType;
import com.example.wallet_service.entity.Wallet;
import com.example.wallet_service.exception.WalletValidationException;
import com.example.wallet_service.service.WalletValidator;
import com.example.wallet_service.service.impl.WalletValidatorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
public class WalletValidatorTest {

    @InjectMocks
    private WalletValidatorImpl walletValidator;

    /** Валидация с null кошельком **/
    @Test
    void validateWalletProcess_WhenWalletIsNull_ThrowsWalletValidationException() {

        Wallet wallet = null;
        BigDecimal amount = BigDecimal.valueOf(100);
        OperationType type = OperationType.WITHDRAW;


        WalletValidationException exception = assertThrows(
                WalletValidationException.class,
                () -> walletValidator.validateWalletProcess(type, amount, wallet)
        );

        Assertions.assertEquals("Wallet not found", exception.getMessage());
    }

    /** Недостаточный баланс при списании **/
    @Test
    void validateWalletProcess_WhenInsufficientBalance_ThrowsWalletValidationException() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(50)); // Баланс меньше суммы списания
        BigDecimal amount = BigDecimal.valueOf(100);
        OperationType type = OperationType.WITHDRAW;


        WalletValidationException exception = assertThrows(
                WalletValidationException.class,
                () -> walletValidator.validateWalletProcess(type, amount, wallet)
        );

        Assertions.assertEquals("Insufficient balance", exception.getMessage());
    }

    /** Успешная валидация при пополнении **/
    @Test
    void validateWalletProcess_WhenDeposit_DoesNotThrowException() {
        // Arrange
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(100));
        BigDecimal amount = BigDecimal.valueOf(50);
        OperationType type = OperationType.DEPOSIT;

        // Act & Assert
        assertDoesNotThrow(() -> walletValidator.validateWalletProcess(type, amount, wallet));
    }




}
