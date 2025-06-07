    package com.example.wallet_service.mapper;

    import com.example.wallet_service.dto.WalletResponse;
    import com.example.wallet_service.entity.Wallet;
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;

    @Mapper(componentModel = "spring")
    public interface WalletMapper {

        @Mapping(target = "balance", source = "balance")
        @Mapping(target = "walletKey", source = "walletKey")
        WalletResponse toResponse(Wallet wallet);
    }