package com.example.wallet_service.dto.wrappers;

import java.util.List;

import com.example.wallet_service.dto.WalletResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseWrapper {

    private WalletResponse walletResponse;

    private List<String> errors;

    public static ResponseWrapper ok(WalletResponse walletResponse) {
        return ResponseWrapper.builder()
                .walletResponse(walletResponse)
                .errors(null)
                .build();
    }

    public static ResponseWrapper error(String errorMessage) {
        return ResponseWrapper.builder()
                .walletResponse(null)
                .errors(List.of(errorMessage))
                .build();
    }

    public static ResponseWrapper listError(List<String> errorMessages) {
        return ResponseWrapper.builder()
                .walletResponse(null)
                .errors(errorMessages)
                .build();
    }
}