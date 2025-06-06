package com.example.wallet_service.exception;

public class WalletValidationException extends RuntimeException {

    public WalletValidationException(final String message) {

        super(message);
    }
}
