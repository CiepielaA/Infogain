package com.interview.infogain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.infogain.model.Transaction;
import com.interview.infogain.service.RewardPointCalculator;
import com.interview.infogain.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardPointController.class)
public class RewardPointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private RewardPointCalculator rewardPointCalculator;

    @Test
    void getRewardPointsPerCustomerPerMonthTest() throws Exception {
        //given
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder()
                        .id(1L)
                        .timestamp(LocalDateTime.now())
                        .customerId(1L)
                        .amount(new BigDecimal("50.00"))
                        .build(),
                Transaction.builder()
                        .id(2L)
                        .timestamp(LocalDateTime.now())
                        .customerId(2L)
                        .amount(new BigDecimal("100.00"))
                        .build()
        );

        HashMap<String, Integer> rewardPoints = new HashMap<>();
        rewardPoints.put("Jan ", 20);
        rewardPoints.put("Feb ", 30);
        String expectedResponse = objectMapper.writeValueAsString(rewardPoints);

        //when
        when(transactionService.getAllTransactionsBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(transactions);
        when(rewardPointCalculator.pointsPerCustomerPerMonth(transactions)).thenReturn(rewardPoints);

        //then
        mockMvc.perform(get("/rewardPoints/perMonths"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
        verify(transactionService, times(1)).getAllTransactionsBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(rewardPointCalculator, times(1)).pointsPerCustomerPerMonth(transactions);
    }

    @Test
    void getRewardPointsPerCustomerTest() throws Exception {
        //given
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder()
                        .id(1L)
                        .timestamp(LocalDateTime.now())
                        .customerId(1L)
                        .amount(new BigDecimal("50.00"))
                        .build(),
                Transaction.builder()
                        .id(2L)
                        .timestamp(LocalDateTime.now())
                        .customerId(2L)
                        .amount(new BigDecimal("100.00"))
                        .build()
        );
        HashMap<Long, Integer> rewardPoints = new HashMap<>();
        rewardPoints.put(1L, 20);
        rewardPoints.put(2L, 30);
        String expectedResponse = objectMapper.writeValueAsString(rewardPoints);

        //when
        when(transactionService.getAllTransactionsBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(transactions);
        when(rewardPointCalculator.pointsPerCustomer(transactions)).thenReturn(rewardPoints);

        //then
        mockMvc.perform(get("/rewardPoints/total"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
        verify(transactionService, times(1)).getAllTransactionsBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(rewardPointCalculator, times(1)).pointsPerCustomer(transactions);
    }
}
