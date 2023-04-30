package com.interview.infogain.controller;

import com.interview.infogain.model.Transaction;
import com.interview.infogain.service.RewardPointCalculator;
import com.interview.infogain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/rewardPoints")
public class RewardPointController {

    private final TransactionService transactionService;
    private final RewardPointCalculator rewardPointCalculator;

    @GetMapping("/perMonths")
    public Map<String, Integer> getRewardPointsPerCustomerPerMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeMonthsEarlier = now.minusMonths(3);

        List<Transaction> transactions = transactionService.getAllTransactionsBetween(threeMonthsEarlier, now);

        HashMap<String, Integer> rewardPoints = rewardPointCalculator.pointsPerCustomerPerMonth(transactions);
        log.info("Successfully returned reward points per customer per months from {} to {}}", now, threeMonthsEarlier);
        return rewardPoints;
    }

    @GetMapping("/total")
    public Map<Long, Integer> getRewardPointsPerCustomer() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeMonthsEarlier = now.minusMonths(3);

        List<Transaction> transactions = transactionService.getAllTransactionsBetween(threeMonthsEarlier, now);

        HashMap<Long, Integer> rewardPoints = rewardPointCalculator.pointsPerCustomer(transactions);
        log.info("Successfully returned total reward points per customers from {} to {}", now, threeMonthsEarlier);
        return rewardPoints;
    }
}
