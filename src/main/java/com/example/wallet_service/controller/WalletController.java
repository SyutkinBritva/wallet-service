package com.example.wallet_service.controller;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.dto.wrappers.ResponseWrapper;
import com.example.wallet_service.service.WalletService;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для работы с кошельком
 */
@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    /**
     * Контроллер для создания нового кошелька. Создан для теста и быстрого добавления нового кошелька в базу.
     *
     * @return данные кошелька (UUID & баланс)
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> createWallet() {
        log.info("Create wallet request");
        WalletResponse wallet = walletService.createWallet();
        log.info("Create wallet response {}", wallet);
        return ResponseEntity.ok(ResponseWrapper.ok(wallet));
    }

    /**
     * Контроллер для создания нового кошелька. Создан для теста и быстрого добавления нового кошелька в базу.
     *
     * @param request входящие данные (UUID, баланс, тип операции)
     * @return данные кошелька (UUID & баланс)
     */
    @PostMapping("/transaction")
    public ResponseEntity<ResponseWrapper> processTransaction(
            @Valid
            @RequestBody
            WalletRequest request) {
        log.info("Process transaction wallet request");
        WalletResponse wallet = walletService.processTransaction(request);
        log.info("Process transaction wallet response {}", wallet);
        return ResponseEntity.ok(ResponseWrapper.ok(wallet));
    }

    /**
     * Получение текущего баланса по ключу кошелька.
     *
     * @param walletKey строковый ключ (UUID) кошелька
     * @return обёртка {@link ResponseWrapper} с балансом и ключом кошелька
     */
    @GetMapping("/{walletKey}")
    public ResponseEntity<ResponseWrapper> getBalance(
            @PathVariable
            @NotEmpty
            String walletKey) {
        log.info("Get wallet balance request, walletKey {}", walletKey);
        WalletResponse wallet = walletService.getBalance(walletKey);
        log.info("Get wallet balance response {}", wallet);
        return ResponseEntity.ok(ResponseWrapper.ok(wallet));
    }
}
