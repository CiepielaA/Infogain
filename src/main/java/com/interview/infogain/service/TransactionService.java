package com.interview.infogain.service;

import com.interview.infogain.exception.ResourceNotFoundException;
import com.interview.infogain.model.Transaction;
import com.interview.infogain.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private static final String TRANSACTION_NOT_FOUND_MESSAGE = "Transaction not found, id: ";

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsBetween(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTimestampBetween(start, end);
    }

    public Transaction getTransactionById(long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE + id));
    }

    public List<Transaction> getAllTransactionsForCustomer(long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(long id, Transaction transaction) {
        Transaction existingTransaction = transactionRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TRANSACTION_NOT_FOUND_MESSAGE + id));

        existingTransaction.setTimestamp(transaction.getTimestamp());
        existingTransaction.setCustomerId(transaction.getCustomerId());
        existingTransaction.setAmount(transaction.getAmount());

        return transactionRepository.save(existingTransaction);
    }

    public void deleteTransaction(long id){
        transactionRepository.deleteById(id);
    }
}
