package com.example.wallet_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WalletResponse {

    private UUID walletKey;

    private BigDecimal balance;
}
