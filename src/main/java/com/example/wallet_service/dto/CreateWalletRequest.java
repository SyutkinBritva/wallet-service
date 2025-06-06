package com.example.wallet_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateWalletRequest {

    @Schema(description = "Unique wallet identifier (UUID format)"
            , requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID walletKey;

    @Schema(description = "Initial balance (default is 0)"
            , defaultValue = "0")
    private BigDecimal balance = BigDecimal.ZERO;
}
