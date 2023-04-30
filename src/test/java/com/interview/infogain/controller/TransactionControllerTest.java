package com.interview.infogain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.infogain.exception.ResourceNotFoundException;
import com.interview.infogain.model.Transaction;
import com.interview.infogain.service.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TransactionService transactionService;

    private final String TRANSACTION_ENDPOINT = "/transactions";

    @Test
    public void shouldReturnAllTransactions() throws Exception {
        //given
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, LocalDateTime.of(2022, 3, 30, 10, 30), 123L, BigDecimal.valueOf(100.0)),
                new Transaction(2L, LocalDateTime.of(2022, 3, 31, 9, 15), 456L, BigDecimal.valueOf(50.0)),
                new Transaction(3L, LocalDateTime.of(2022, 4, 1, 11, 0), 789L, BigDecimal.valueOf(200.0))
        );
        String expectedResponse = objectMapper.writeValueAsString(transactions);

        //when
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        //then
        mockMvc.perform(get(TRANSACTION_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
        verify(transactionService, times(1)).getAllTransactions();
    }

    @Test
    public void shouldCallGetAllTransactionsForCustomerMethodWithProperCustomerId() throws Exception {
        //given
        long customerId = 1L;

        //when
        when(transactionService.getAllTransactionsForCustomer(customerId)).thenReturn(Collections.emptyList());

        //then
        mockMvc.perform(get(TRANSACTION_ENDPOINT + "/customers/{customerId}", customerId))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).getAllTransactionsForCustomer(customerId);
    }

    @Test
    public void shouldCreateTransaction() throws Exception {
        //given
        Transaction transaction = new Transaction(1L, LocalDateTime.of(2022, 3, 30, 10, 30), 123L, BigDecimal.valueOf(100.0));

        //when
        when(transactionService.createTransaction(transaction)).thenReturn(transaction);

        //then
        mockMvc.perform(post(TRANSACTION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated());
        verify(transactionService, times(1)).createTransaction(transaction);
    }

    @Test
    public void shouldReturnBadRequestWhenNoMandatoryFieldsProvidedForPOST() throws Exception {
        //when
        String response = mockMvc.perform(post(TRANSACTION_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Transaction())))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        verify(transactionService, never()).createTransaction(any());
        Assertions.assertTrue(response.contains("amount is mandatory"));
        Assertions.assertTrue(response.contains("customerId is mandatory"));
        Assertions.assertTrue(response.contains("timestamp is mandatory"));
    }

    @Test
    public void shouldUpdateTransaction() throws Exception {
        //given
        long transactionId = 1L;
        Transaction transaction = new Transaction(transactionId, LocalDateTime.of(2022, 3, 30, 10, 30), 123L, BigDecimal.valueOf(100.0));

        //when
        when(transactionService.updateTransaction(transactionId, transaction)).thenReturn(transaction);

        //then
        mockMvc.perform(put(TRANSACTION_ENDPOINT + "/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isOk());
        verify(transactionService, times(1)).updateTransaction(transactionId, transaction);
    }

    @Test
    public void shouldReturnNoFoundWhenNoSuchTransaction() throws Exception {
        //given
        long transactionId = 1L;
        Transaction transaction = new Transaction(transactionId, LocalDateTime.of(2022, 3, 30, 10, 30), 123L, BigDecimal.valueOf(100.0));

        //when
        when(transactionService.updateTransaction(transactionId, transaction)).thenThrow(ResourceNotFoundException.class);

        //then
        mockMvc.perform(put(TRANSACTION_ENDPOINT + "/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isNotFound());
        verify(transactionService, times(1)).updateTransaction(transactionId, transaction);
    }

    @Test
    public void shouldReturnBadRequestWhenNoMandatoryFieldsProvidedForPUT() throws Exception {
        //given
        long transactionId = 1L;

        //when
        String response = mockMvc.perform(put(TRANSACTION_ENDPOINT + "/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Transaction())))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        verify(transactionService, never()).createTransaction(any());
        Assertions.assertTrue(response.contains("amount is mandatory"));
        Assertions.assertTrue(response.contains("customerId is mandatory"));
        Assertions.assertTrue(response.contains("timestamp is mandatory"));
    }

    @Test
    public void shouldDeleteTransaction() throws Exception {
        //given
        long transactionId = 1L;

        //when
        doNothing().when(transactionService).deleteTransaction(transactionId);

        //then
        mockMvc.perform(delete(TRANSACTION_ENDPOINT + "/{id}", transactionId))
                .andExpect(status().isNoContent());
        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }

}