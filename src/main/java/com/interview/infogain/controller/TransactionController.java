package com.interview.infogain.controller;

import com.interview.infogain.model.Transaction;
import com.interview.infogain.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{customerId}")
    public List<Transaction> getAllTransactionsForCustomer(@PathVariable long customerId) {
        return transactionService.getAllTransactionsForCustomer(customerId);
    }

    @GetMapping("/{id}")
    public Transaction getTransactionById(@PathVariable long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable long id, @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }

}