package com.example.wallet_service.mapper;

import com.example.wallet_service.dto.CreateWalletRequest;
import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(target = "id", ignore = true)
    Wallet toEntity(WalletRequest request);

    @Mapping(target = "id", ignore = true)
    Wallet toEntity(CreateWalletRequest request);

    @Mapping(target = "balance", source = "balance")
    WalletResponse toResponse(Wallet wallet);

}