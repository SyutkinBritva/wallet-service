package com.example.wallet_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletRequest {

    @NotEmpty(message = "walletKey must not be null or empty")
    @Schema(description = "Unique wallet identifier (UUID format)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String walletKey;

    @NotEmpty(message = "Amount must not be null or empty")
    @Schema(description = "Type of financial operation", requiredMode = Schema.RequiredMode.REQUIRED)
    private String operationType;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than zero")
    @Schema(description = "Transaction amount (positive decimal value)", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
}
