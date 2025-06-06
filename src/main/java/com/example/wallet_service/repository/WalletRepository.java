package com.example.wallet_service.repository;


import com.example.wallet_service.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByWalletKey(UUID walletKey);

    boolean existsByWalletKey(UUID walletKey);
}
