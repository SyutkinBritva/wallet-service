package com.example.wallet_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.example.wallet_service.entity.Wallet;
import org.springframework.mock.web.MockHttpServletResponse;

public class WalletControllerTest extends AbstractSpringTest {

    /**
     * Проверяет успешное создание нового кошелька
     **/
    @Test
    void shouldCreateWallet() throws Exception {
        mockMvc.perform(post(PATH + "/create"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletResponse.walletKey").isNotEmpty())
                .andExpect(jsonPath("$.walletResponse.balance").value(0.00));
    }

    /**
     * Проверяет успешное пополнение кошелька
     **/
    @Test
    void shouldDepositSuccessfully() throws Exception {
        Wallet wallet = saveTestWallet(BigDecimal.valueOf(10.00));

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": "%s",
                                    "operationType": "DEPOSIT",
                                    "amount": 15.00
                                }
                                """.formatted(wallet.getWalletKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletResponse.balance").value(25.00))
                .andExpect(jsonPath("$.walletResponse.errors").doesNotExist());
    }

    /**
     * Проверяет успешное списание средств с кошелька
     **/
    @Test
    void shouldWithdrawSuccessfully() throws Exception {
        Wallet wallet = saveTestWallet(BigDecimal.valueOf(20.00));

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": "%s",
                                    "operationType": "WITHDRAW",
                                    "amount": 10.00
                                }
                                """.formatted(wallet.getWalletKey())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletResponse.balance").value(10.00))
                .andExpect(jsonPath("$.walletResponse.errors").doesNotExist());
    }

    /**
     * Проверяет ошибку при списании суммы, превышающей баланс
     **/
    @Test
    void shouldFailWithdrawDueToInsufficientBalance() throws Exception {
        Wallet wallet = saveTestWallet(BigDecimal.valueOf(5.00));

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": "%s",
                                    "operationType": "WITHDRAW",
                                    "amount": 10.00
                                }
                                """.formatted(wallet.getWalletKey())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Insufficient balance"));
    }

    /**
     * Проверяет получение текущего баланса по `walletKey`
     **/
    @Test
    void shouldReturnWalletBalance() throws Exception {
        Wallet wallet = saveTestWallet(BigDecimal.valueOf(99.99));

        mockMvc.perform(get(PATH + "/" + wallet.getWalletKey()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletResponse.balance").value(99.99))
                .andExpect(jsonPath("$.walletResponse.walletKey").value(wallet.getWalletKey().toString()));
    }

    /**
     * Проверяет ошибку 404 при запросе несуществующего кошелька
     **/
    @Test
    void shouldReturnErrorWhenWalletNotFound() throws Exception {
        UUID nonExistingKey = UUID.randomUUID();

        mockMvc.perform(get(PATH + "/" + nonExistingKey))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.containsString("wallet not found")));
    }

    /**
     * Проверяет ошибку 400 при пустом `walletKey` (валидация)
     **/
    @Test
    void shouldReturnErrorOnValidationFailure() throws Exception {
        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": "",
                                    "operationType": "WITHDRAW",
                                    "amount": 10.00
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    /**
     * Проверяет ошибку 500 при недопустимом формате ключа кошелька
     **/
    @Test
    void shouldReturnErrorWhenWalletKeyIsInvalidFormat() throws Exception {
        mockMvc.perform(get(PATH + "/invalid-uuid-format"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0]").value("Inserted invalid wallet key"));
    }

    /**
     * Проверяет ошибку при недостатке средств
     **/
    @Test
    void shouldReturnErrorWhenWalletHasInsufficientFunds() throws Exception {
        Wallet wallet = saveTestWallet(BigDecimal.valueOf(1.00));

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": "%s",
                                    "operationType": "WITHDRAW",
                                    "amount": 100.00
                                }
                                """.formatted(wallet.getWalletKey())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Insufficient balance"));
    }

    /**
     * Проверяет ошибку 400 при передаче пустого JSON
     **/
    @Test
    void shouldReturnBadRequestOnEmptyJson() throws Exception {
        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    /**
     * Проверяет ошибку 400 при отсутствии обязательных полей в теле запроса
     **/
    @Test
    void shouldReturnBadRequestOnMissingFields() throws Exception {
        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    /**
     * Проверяет ошибку 500 при некорректном значении `operationType`
     **/
    @Test
    void shouldReturnErrorOnInvalidOperation() throws Exception {
        final Wallet testWallet = saveTestWallet(BigDecimal.ZERO);

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": "%s",
                                    "operationType": "INVALID_OP",
                                    "amount": 10.00
                                }
                                """.formatted(testWallet.getWalletKey())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Unsupported operation type: INVALID_OP"));
    }

    /**
     * Проверяет ошибку 400 при попытке передать отрицательное значение `amount`
     **/
    @Test
    void shouldReturnErrorWhenAmountNegative() throws Exception {
        final Wallet testWallet = saveTestWallet();

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "walletKey": "%s",
                                    "operationType": "DEPOSIT",
                                    "amount": -5.00
                                }
                                """.formatted(testWallet.getWalletKey())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("Amount must be greater than zero"));
    }

    /**
     * Проверка на конкурентное изменение баланса
     **/
    @RepeatedTest(20)
    void shouldReturnBadRequestOnConcurrentBalanceModification() throws Exception {
        Wallet wallet = saveTestWallet(BigDecimal.valueOf(100));

        int threadCount = 2;
        CyclicBarrier barrier = new CyclicBarrier(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Runnable task = () -> {
            try {
                barrier.await(); // Ждём, пока оба потока будут готовы

                MockHttpServletResponse response = mockMvc.perform(post(PATH + "/transaction")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("""
                                        {
                                            "walletKey": "%s",
                                            "operationType": "WITHDRAW",
                                            "amount": 90.00
                                        }
                                        """, wallet.getWalletKey())))
                        .andReturn()
                        .getResponse();

                int status = response.getStatus();
                assertTrue(
                        status == HttpStatus.OK.value() || status == HttpStatus.BAD_REQUEST.value(),
                        "Expected status 200 or 400 but was: " + status
                );
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        };

        new Thread(task).start();
        new Thread(task).start();

        latch.await();

        Wallet updatedWallet = walletRepository.findByWalletKey(wallet.getWalletKey()).orElseThrow();
        BigDecimal balance = updatedWallet.getBalance();

        assertEquals(BigDecimal.valueOf(10.00).setScale(2), balance);
    }

    /**
     * Проверка на нечисловое значение amount
     **/
    @Test
    void shouldReturnErrorWhenAmountIsNotNumeric() throws Exception {
        Wallet wallet = saveTestWallet();

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "walletKey": "%s",
                                    "operationType": "DEPOSIT",
                                    "amount": "ten"
                                }
                                """, wallet.getWalletKey())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.containsString("Cannot deserialize value")));
    }

    /**
     * Проверка на null в OperationType
     **/
    @Test
    void shouldReturnErrorWhenOperationTypeIsNull() throws Exception {
        Wallet wallet = saveTestWallet();

        mockMvc.perform(post(PATH + "/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "walletKey": "%s",
                                    "operationType": null,
                                    "amount": 10.00
                                }
                                """, wallet.getWalletKey())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                // В сообщении обычно будет что-то вроде "must not be null" или "Operation type must be specified"
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.anyOf(
                        org.hamcrest.Matchers.containsString("must not be null"),
                        org.hamcrest.Matchers.containsString("Operation type must be specified")
                )));
    }

    private Wallet saveTestWallet(BigDecimal balance) {
        Wallet wallet = new Wallet();
        wallet.setWalletKey(UUID.randomUUID());
        wallet.setBalance(balance.setScale(2));
        return walletRepository.save(wallet);
    }

    private Wallet saveTestWallet() {
        return saveTestWallet(BigDecimal.valueOf(0.00));
    }
}