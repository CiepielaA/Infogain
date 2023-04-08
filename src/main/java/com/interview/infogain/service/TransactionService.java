package com.interview.infogain.service;

import com.interview.infogain.exception.ResourceNotFoundException;
import com.interview.infogain.model.Transaction;
import com.interview.infogain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllTransactionsForCustomer(Long id) {
        return transactionRepository.findByCustomerId(id);
    }

    public List<Transaction> getAllTransactionsBetween(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTimestampBetween(start, end);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction updateTransaction(Long id, Transaction transactionDetails) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        transaction.setTimestamp(transactionDetails.getTimestamp());
        transaction.setCustomerId(transactionDetails.getCustomerId());
        transaction.setAmount(transactionDetails.getAmount());

        return transactionRepository.save(transaction);
    }

//    public List<Transaction> getTransactionsForCustomerFromLastThreeMonths(Long customerId) {
//        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
//        LocalDateTime now = LocalDateTime.now();
//
//        now.with(TemporalAdjusters.lastDayOfMonth())
//                .wi
//        return transactionRepository.findByCustomerIdAndTimestampBetween(customerId, threeMonthsAgo, now);
//    }
}
