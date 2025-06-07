package com.example.wallet_service;

import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.entity.Wallet;
import com.example.wallet_service.mapper.WalletMapper;
import com.example.wallet_service.mapper.WalletMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WalletMapperTest {

    private final WalletMapper walletMapper = new WalletMapperImpl();

    /** Корректность маппинга полей balance и walletKey **/
    @Test
    void toResponse_ShouldMapWalletToWalletResponseCorrectly(){
        //given
        Wallet wallet = new Wallet();
        UUID key = UUID.randomUUID();
        wallet.setBalance(BigDecimal.valueOf(1000));
        wallet.setWalletKey(key);

        //when
        WalletResponse response = walletMapper.toResponse(wallet);

        //then
        Assertions.assertNotNull(response);
        assertEquals(wallet.getBalance(), response.getBalance());
        assertEquals(wallet.getWalletKey(), response.getWalletKey());
    }

    /** обработка Null на входе **/
    @Test
    void toResponse_ShouldHandleNullInput() {
        assertNull(walletMapper.toResponse(null));
    }
}
