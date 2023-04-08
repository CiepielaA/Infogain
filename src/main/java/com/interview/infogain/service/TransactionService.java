package com.interview.infogain.service;

import com.interview.infogain.exception.ResourceNotFoundException;
import com.interview.infogain.model.Transaction;
import com.interview.infogain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsBetween(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTimestampBetween(start, end);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Transaction> getAllTransactionsForCustomer(Long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    public List<Transaction> getAllTransactionsForCustomerBetween(Long customerId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByCustomerIdAndTimestampBetween(customerId, start, end);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transaction) {
        Transaction existingTransaction = transactionRepository
                .findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        existingTransaction.setTimestamp(transaction.getTimestamp());
        existingTransaction.setCustomerId(transaction.getCustomerId());
        existingTransaction.setAmount(transaction.getAmount());

        return transactionRepository.save(existingTransaction);
    }
}
