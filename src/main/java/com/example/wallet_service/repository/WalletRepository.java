package com.example.wallet_service.repository;


import com.example.wallet_service.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    /**
     * Запрос в базу данных по кошельку и его pessimistic lock до завершения операции.
     *
     * @param walletKey UUID
     * @return кошелёк из базы
     */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from Wallet w where w.walletKey = ?1")
    Optional<Wallet> getWalletForUpdate(UUID walletKey);

    /**
     * Запрос в базу данных по кошельку без лока.
     *
     * @param walletKey UUID
     * @return кошелёк из базы
     */
    Optional<Wallet> findByWalletKey(UUID walletKey);

}
