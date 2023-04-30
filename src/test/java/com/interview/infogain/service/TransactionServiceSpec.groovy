package com.interview.infogain.service

import com.interview.infogain.exception.ResourceNotFoundException
import com.interview.infogain.model.Transaction
import com.interview.infogain.repository.TransactionRepository
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class TransactionServiceSpec extends Specification {

    TransactionRepository transactionRepository = Mock()

    @Subject
    TransactionService transactionService = new TransactionService(transactionRepository)

    def "should get transaction by id"() {
        given:
        long id = 1L
        Transaction transaction = Transaction.builder()
                .id(id)
                .timestamp(LocalDateTime.now())
                .customerId(id)
                .amount(BigDecimal.valueOf(100))
                .build()
        1 * transactionRepository.findById(id) >> Optional.of(transaction)

        expect:
        transactionService.getTransactionById(id) == transaction
    }

    def "should throw ResourceNotFoundException when transaction not found by id"() {
        given:
        long id = 1L
        1 * transactionRepository.findById(id) >> Optional.empty()

        when:
        transactionService.getTransactionById(id)

        then:
        thrown(ResourceNotFoundException)
    }

    def "should update transaction"() {
        given:
        long id = 1L
        Transaction transaction = Transaction.builder()
                .id(id)
                .timestamp(LocalDateTime.now())
                .customerId(id)
                .amount(BigDecimal.valueOf(100))
                .build()

        Transaction updatedTransaction = Transaction.builder()
                .id(id)
                .timestamp(LocalDateTime.now())
                .customerId(2)
                .amount(BigDecimal.valueOf(200))
                .build()
        1 * transactionRepository.findById(id) >> Optional.of(transaction)
        1 * transactionRepository.save(_ as Transaction) >> updatedTransaction

        when:
        Transaction result = transactionService.updateTransaction(id, transaction)

        then:
        result == updatedTransaction
    }

    def "should throw ResourceNotFoundException when no such transaction"() {
        given:
        long id = 1L
        1 * transactionRepository.findById(id) >> Optional.empty()
        0 * transactionRepository.save(_)

        when:
        transactionService.updateTransaction(id, new Transaction())

        then:
        thrown(ResourceNotFoundException)
    }
}