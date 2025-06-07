package com.example.wallet_service.service.impl;

import com.example.wallet_service.common.OperationType;
import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.entity.Wallet;
import com.example.wallet_service.mapper.WalletMapper;
import com.example.wallet_service.repository.WalletRepository;
import com.example.wallet_service.service.WalletService;
import com.example.wallet_service.service.WalletValidator;

import lombok.RequiredArgsConstructor;

import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;

    private final WalletValidator walletValidator;

    /**
     * Выполнение транзакции.
     *
     * @param request входящие данные (UUID, баланс, тип операции)
     * @return данные кошелька (UUID & баланс)
     */
    @Override
    @Transactional
    public WalletResponse processTransaction(WalletRequest request) {

        OperationType type = OperationType.from(request.getOperationType());
        UUID uuidWalletKey = parseWalletUUIDfromString(request.getWalletKey());

        Wallet wallet = walletRepository.getWalletForUpdate(uuidWalletKey)
                .orElse(null);

        walletValidator.validateWalletProcess(type, request.getAmount(), wallet);

        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal amount = request.getAmount();
        OperationType operationType = OperationType.from(request.getOperationType());

        switch (operationType) {
            case DEPOSIT -> wallet.setBalance(currentBalance.add(amount));
            case WITHDRAW -> wallet.setBalance(currentBalance.subtract(amount));
        }

        walletRepository.save(wallet);

        return new WalletResponse(uuidWalletKey, wallet.getBalance());
    }

    /**
     * Получение текущего баланса кошелька по его ключу.
     *
     * @param walletKey строковое представление UUID ключа кошелька
     * @return объект ответа, содержащий ключ кошелька и его текущий баланс
     */
    @Override
    @Transactional(readOnly = true)
    public WalletResponse getBalance(String walletKey) {

        final UUID uuidWalletKey = parseWalletUUIDfromString(walletKey);
        Wallet wallet = walletRepository.findByWalletKey(uuidWalletKey)
                .orElseThrow(() -> new ObjectNotFoundException(uuidWalletKey, "wallet not found"));
        return walletMapper.toResponse(wallet);
    }

    /**
     * Создание нового кошелька с нулевым балансом.
     *
     * @return объект ответа с UUID нового кошелька и нулевым балансом
     */
    @Override
    @Transactional
    public WalletResponse createWallet() {

        Wallet wallet = new Wallet();

        wallet.setWalletKey(UUID.randomUUID());
        wallet.setBalance(BigDecimal.ZERO.setScale(2));

        walletRepository.save(wallet);

        return walletMapper.toResponse(wallet);
    }

    /**
     * Преобразование строки в UUID кошелька.
     *
     * @param walletKey строковое значение ключа кошелька
     * @return UUID кошелька
     */
    private UUID parseWalletUUIDfromString(String walletKey) {

        UUID parsedWalletKey;
        try {
            parsedWalletKey = UUID.fromString(walletKey);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException("Inserted invalid wallet key");
        }
        return parsedWalletKey;
    }
}
