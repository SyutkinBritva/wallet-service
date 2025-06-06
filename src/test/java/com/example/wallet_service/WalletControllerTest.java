package com.example.wallet_service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.example.wallet_service.entity.Wallet;

public class WalletControllerTest extends AbstractSpringTest {

    @Test
    void shouldCreateWallet() throws Exception {

        final Wallet testWallet = saveTestWallet();

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "walletKey": "%s",
                            "operationType": "DEPOSIT",
                            "amount": 100.00
                        }
                        """.formatted(testWallet.getWalletKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletResponse.balance").value(110.00))
                .andExpect(jsonPath("$.walletResponse.walletKey").value(testWallet.getWalletKey().toString()))
                .andExpect(jsonPath("$.walletResponse.errors").doesNotExist());
    }


    private Wallet saveTestWallet() {

        final Wallet wallet = new Wallet();
        wallet.setWalletKey(UUID.randomUUID());
        wallet.setBalance(BigDecimal.TEN);
        walletRepository.save(wallet);
        return wallet;
    }
}