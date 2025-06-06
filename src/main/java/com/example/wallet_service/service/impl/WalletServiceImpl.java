package com.example.wallet_service.service.impl;

import com.example.wallet_service.common.OperationType;
import com.example.wallet_service.dto.CreateWalletRequest;
import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.entity.Wallet;
import com.example.wallet_service.mapper.WalletMapper;
import com.example.wallet_service.repository.WalletRepository;
import com.example.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Override
    @Transactional
    public WalletResponse processTransaction(WalletRequest request) {

        Wallet wallet = walletRepository.findByWalletKey(request.getWalletKey())
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found"));

        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal amount = request.getAmount();

        if (request.getOperationType() == OperationType.DEPOSIT) {
            wallet.setBalance(currentBalance.add(amount));
        } else if (request.getOperationType() == OperationType.WITHDRAW) {
            if (currentBalance.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }
            wallet.setBalance(currentBalance.subtract(amount));
        } else {
            throw new IllegalArgumentException("Unsupported operation type");
        }

        walletRepository.save(wallet);

        WalletResponse response = new WalletResponse();
        response.setBalance(wallet.getBalance());
        return response;
    }

    @Override
    @Transactional
    public WalletResponse getBalance(UUID walletKey) {

        Wallet wallet = walletRepository.findByWalletKey(walletKey)
                .orElseThrow(() -> new RuntimeException("wallet not found"));

        return walletMapper.toResponse(wallet);
    }

    @Override
    @Transactional
    public WalletResponse createWallet(CreateWalletRequest request) {

        if (walletRepository.existsByWalletKey(request.getWalletKey())) {
            throw new IllegalArgumentException("Wallet with this key already exists");
        }

        Wallet wallet = walletMapper.toEntity(request);

        Wallet savedWallet = walletRepository.save(wallet);

        return walletMapper.toResponse(savedWallet);
    }

}
