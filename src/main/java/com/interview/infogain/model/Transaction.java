package com.interview.infogain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "timestamp is mandatory")
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @NotNull(message = "customerId is mandatory")
    @Column(name = "customer_id")
    private Long customerId;

    @NotNull(message = "amount is mandatory")
    @PositiveOrZero(message = "amount cannot be negative")
    @Column(name = "amount")
    private BigDecimal amount;

}
