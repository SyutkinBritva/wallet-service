package com.example.wallet_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletResponse {

    private BigDecimal balance;
}
