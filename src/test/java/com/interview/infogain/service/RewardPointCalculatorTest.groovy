package com.interview.infogain.service

import com.interview.infogain.model.Transaction
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class RewardPointCalculatorTest extends Specification {

    @Subject
    RewardPointCalculator calculator = new RewardPointCalculator()

    def "should return 0 when no transactions"() {
        expect:
        0 == calculator.totalPoints([])
    }


    def "should calculate reward points for one transaction"() {
        given:
        def transactions = [
                Transaction.builder().amount(BigDecimal.valueOf(amount)).build()
        ]

        when:
        def result = calculator.totalPoints(transactions)

        then:
        result == expectedResult

        where:
        amount || expectedResult
        -10    || 0
        0      || 0
        50     || 0
        50     || 0
        50.5   || 0
        51     || 1
        100    || 50
        100.9  || 50
        101    || 52
        120    || 90
        1200   || 2250
    }

    def "should calculate reward points for many transaction"() {
        given:
        def transactions = [
                Transaction.builder().amount(BigDecimal.valueOf(120)).build(),
                Transaction.builder().amount(BigDecimal.valueOf(120)).build(),
                Transaction.builder().amount(BigDecimal.valueOf(0)).build(),
                Transaction.builder().amount(BigDecimal.valueOf(50)).build(),
                Transaction.builder().amount(BigDecimal.valueOf(51)).build()
        ]

        when:
        def result = calculator.totalPoints(transactions)

        then:
        result == 181
    }

    def "should create month with 0 points if no transactions in this month"() {
        given:
        def startDate = LocalDateTime.of(2022, 2, 1, 10, 0)
        def endDate = LocalDateTime.of(2022, 3, 10, 8, 0)
        def customerTransactions = []

        when:
        def result = calculator.pointsPerMonth(customerTransactions, startDate, endDate)

        then:
        result.size() == 3
        result['FEBRUARY'] == BigDecimal.valueOf(0)
        result['MARCH'] == BigDecimal.valueOf(0)
        result['APRIL'] == BigDecimal.valueOf(0)
    }

    def "should create month with 0 points if there are only transactions with amount < FIRST_THRESHOLD "() {
        given:
        def startDate = LocalDateTime.of(2022, 2, 1, 10, 0)
        def endDate = LocalDateTime.of(2022, 3, 10, 8, 0)
        def transaction1 = Transaction.builder()
                .timestamp(LocalDateTime.of(2022, 3, 1, 10, 0))
                .amount(BigDecimal.valueOf(RewardPointCalculator.FIRST_THRESHOLD))
                .build()
        def transaction2 = Transaction.builder()
                .timestamp(LocalDateTime.of(2022, 3, 2, 12, 0))
                .amount(BigDecimal.valueOf(0))
                .build()
        def customerTransactions = [transaction1, transaction2]

        when:
        def result = calculator.pointsPerMonth(customerTransactions, startDate, endDate)

        then:
        result.size() == 2
        result['FEBRUARY'] == BigDecimal.valueOf(0)
        result['MARCH'] == BigDecimal.valueOf(0)
    }

    def "should calculate points per months"() {
        given:
        def startDate = LocalDateTime.of(2022, 2, 1, 10, 0)
        def endDate = LocalDateTime.of(2022, 5, 10, 8, 0)
        def transaction1 = Transaction.builder()
                .timestamp(LocalDateTime.of(2022, 3, 1, 10, 0))
                .amount(BigDecimal.valueOf(120))
                .build()
        def transaction2 = Transaction.builder()
                .timestamp(LocalDateTime.of(2022, 3, 2, 12, 0))
                .amount(BigDecimal.valueOf(120))
                .build()
        def transaction3 = Transaction.builder()
                .timestamp(LocalDateTime.of(2022, 4, 1, 15, 0))
                .amount(BigDecimal.valueOf(0))
                .build()
        def transaction4 = Transaction.builder()
                .timestamp(LocalDateTime.of(2022, 4, 1, 15, 0))
                .amount(BigDecimal.valueOf(25))
                .build()
        def transaction5 = Transaction.builder()
                .timestamp(LocalDateTime.of(2022, 5, 10, 8, 0))
                .amount(BigDecimal.valueOf(51))
                .build()

        def customerTransactions = [transaction1, transaction2, transaction3, transaction5]

        when:
        def result = calculator.pointsPerMonth(customerTransactions, startDate, endDate)

        then:
        result.size() == 4
        result['FEBRUARY'] == BigDecimal.valueOf(0)
        result['MARCH'] == BigDecimal.valueOf(180)
        result['APRIL'] == BigDecimal.valueOf(0)
        result['MAY'] == BigDecimal.valueOf(1)
    }
}