package com.interview.infogain.controller;

import com.interview.infogain.model.Transaction;
import com.interview.infogain.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/customers/{customerId}")
    public List<Transaction> getAllTransactionsForCustomer(@PathVariable long customerId) {
        return transactionService.getAllTransactionsForCustomer(customerId);
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    ////////////////////////
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<Transaction> createTransaction(@Valid @RequestBody List<Transaction> transactions) {
        return transactionService.createTransactions(transactions);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable long id, @Valid @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable long id) {
        transactionService.deleteTransaction(id);
    }

}