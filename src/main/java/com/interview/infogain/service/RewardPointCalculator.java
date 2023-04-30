package com.interview.infogain.service;

import com.interview.infogain.model.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;

@Service
public class RewardPointCalculator {

    public static final BigDecimal FIRST_THRESHOLD = BigDecimal.valueOf(50);
    public static final int FIRST_THRESHOLD_POINTS = 1;
    public static final BigDecimal SECOND_THRESHOLD = BigDecimal.valueOf(100);
    public static final int SECOND_THRESHOLD_POINTS = 2;

    public int totalPoints(List<Transaction> transactions) {
        int points = 0;

        for (Transaction transaction : transactions) {
            points += calculatePointsForAmount(transaction.getAmount());
        }

        return points;
    }

    public HashMap<Long, Integer> pointsPerCustomer(List<Transaction> transactions) {
        HashMap<Long, Integer> customerIdToPoints = new HashMap<>();

        for (Transaction transaction : transactions) {
            int points = calculatePointsForAmount(transaction.getAmount());
            customerIdToPoints.merge(transaction.getCustomerId(), points, Integer::sum);
        }

        return customerIdToPoints;
    }

    public HashMap<String, Integer> pointsPerCustomerPerMonth(List<Transaction> transactions) {
        HashMap<String, Integer> customerPerMonthPoints = new HashMap<>();

        for (Transaction transaction : transactions) {
            int points = calculatePointsForAmount(transaction.getAmount());
            customerPerMonthPoints.merge(createKey(transaction), points, Integer::sum);
        }

        return customerPerMonthPoints;
    }

    private String createKey(Transaction transaction){
        return transaction.getTimestamp().getMonth().name() + " customerId: " + transaction.getCustomerId().toString();
    }

    public HashMap<String, Integer> pointsPerMonth(List<Transaction> transactions, LocalDateTime start, LocalDateTime end) {

        HashMap<String, Integer> sumByMonths = prepareMap(start, end);

        for (Transaction transaction : transactions) {
            String key = transaction.getTimestamp().getMonth().name();
            sumByMonths.put(key, sumByMonths.get(key) + calculatePointsForAmount(transaction.getAmount()));
        }
        return sumByMonths;
    }

    private HashMap<String, Integer> prepareMap(LocalDateTime start, LocalDateTime end) {
        HashMap<String, Integer> sumByMonths = new HashMap<>();
        for (int i = start.getMonth().getValue(); i <= end.getMonth().getValue(); i++){
            sumByMonths.put(Month.of(i).name(), 0);
        }
        return sumByMonths;
    }

    private int calculatePointsForAmount(BigDecimal amount) {
        int points = 0;

        if (amount.compareTo(FIRST_THRESHOLD) > 0) {
            points += Math.min(
                    SECOND_THRESHOLD.subtract(FIRST_THRESHOLD).intValue(),
                    getPointsForThreshold(amount, FIRST_THRESHOLD, FIRST_THRESHOLD_POINTS));
        }
        if (amount.compareTo(SECOND_THRESHOLD) > 0) {
            points += getPointsForThreshold(amount, SECOND_THRESHOLD, SECOND_THRESHOLD_POINTS);
        }

        return points;
    }

    private int getPointsForThreshold(BigDecimal amount, BigDecimal threshold, int thresholdPoints) {
        return amount.subtract(threshold).intValue() * thresholdPoints;
    }
}
