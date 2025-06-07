package com.example.wallet_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet_key", nullable = false, unique = true, columnDefinition = "UUID кошелька")
    private UUID walletKey;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2, columnDefinition = "Баланс кошелька")
    private BigDecimal balance;

}