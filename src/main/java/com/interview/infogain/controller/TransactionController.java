package com.interview.infogain.controller;

import com.interview.infogain.model.Transaction;
import com.interview.infogain.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        log.info("Found {} transactions", transactions.size());
        return transactions;
    }

    @GetMapping("/customers/{customerId}")
    public List<Transaction> getAllTransactionsForCustomer(@PathVariable long customerId) {
        List<Transaction> transactionsForCustomer = transactionService.getAllTransactionsForCustomer(customerId);
        log.info("Found {} transactions for customer with id: {}", transactionsForCustomer.size(), customerId);
        return transactionsForCustomer;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    ////////////////////////
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public List<Transaction> createTransactions(@Valid @RequestBody List<Transaction> transactions) {
//        return transactionService.createTransactions(transactions);
//    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable long id, @Valid @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable long id) {
        transactionService.deleteTransaction(id);
    }

}