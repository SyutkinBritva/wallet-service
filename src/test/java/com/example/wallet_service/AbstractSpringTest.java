package com.example.wallet_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.example.wallet_service.repository.WalletRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
@ContextConfiguration(initializers = TestContainersInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractSpringTest {

    public static final String PATH = "/api/v1/wallet";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WalletRepository walletRepository;

    @BeforeEach
    public void tearDown() {
        walletRepository.deleteAll();
    }
}