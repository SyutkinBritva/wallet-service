package com.example.wallet_service.mapper;

import com.example.wallet_service.dto.WalletRequest;
import com.example.wallet_service.dto.WalletResponse;
import com.example.wallet_service.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(target = "id", ignore = true)
    Wallet toEntity(WalletRequest request);

    WalletResponse toResponse(Wallet wallet);
}