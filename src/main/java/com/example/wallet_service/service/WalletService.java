package com.example.wallet_service.service;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;

import java.util.UUID;

public interface WalletService {

    WalletResponse processTransaction(WalletRequest request);

    WalletResponse getBalance(UUID walletKey);
}
