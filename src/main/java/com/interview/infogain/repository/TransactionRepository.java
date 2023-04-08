package com.interview.infogain.repository;

import com.interview.infogain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerId(Long customerId);

    List<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<Transaction> findByCustomerIdAndTimestampBetween(Long customerId, LocalDateTime startTimestamp, LocalDateTime endTimestamp);
}
