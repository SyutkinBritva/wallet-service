package com.example.wallet_service.controller;

import com.example.wallet_service.dto.CreateWalletRequest;
import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/create")
    public ResponseEntity<WalletResponse> createWallet(@RequestBody CreateWalletRequest request){
        WalletResponse wallet =  walletService.createWallet(request);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/transaction")
    public ResponseEntity<WalletResponse> processTransaction(@RequestBody WalletRequest request) {
        WalletResponse response = walletService.processTransaction(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{walletKey}")
    public ResponseEntity<WalletResponse> getBalance(@PathVariable UUID walletKey){
        WalletResponse response = walletService.getBalance(walletKey);
        return ResponseEntity.ok(response);
    }
}
