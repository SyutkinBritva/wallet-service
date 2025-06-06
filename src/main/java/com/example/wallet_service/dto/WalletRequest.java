package com.example.wallet_service.dto;

import com.example.wallet_service.common.OperationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletRequest {

    @NotNull(message = "walletKey must not be null")
    @Schema(description = "Unique wallet identifier (UUID format)"
            , requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID walletKey;

    @NotNull(message = "Amount must not be null")
    @Schema(description = "Type of financial operation"
            , requiredMode = Schema.RequiredMode.REQUIRED)
    private OperationType operationType;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than zero")
    @Schema(description = "Transaction amount (positive decimal value)"
            , requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
}
