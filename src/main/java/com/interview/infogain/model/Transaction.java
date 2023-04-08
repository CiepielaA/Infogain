package com.interview.infogain.model;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Positive;
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

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "customer_id")
    private Long customerId;

//    @Positive
    @Column(name = "amount")
    private BigDecimal amount;

}
