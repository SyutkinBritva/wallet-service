package com.example.wallet_service.dto;

import com.example.wallet_service.common.OperationType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletRequest {
    private UUID walletKey;
    private OperationType operationType;
    private BigDecimal balance;
}
