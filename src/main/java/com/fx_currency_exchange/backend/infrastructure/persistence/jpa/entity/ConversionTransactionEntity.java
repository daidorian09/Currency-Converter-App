package com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "conversion_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversionTransactionEntity {

    @Id
    private UUID id;

    private String fromCurrency;

    private String toCurrency;

    private BigDecimal amount;

    private BigDecimal convertedAmount;

    private LocalDateTime timestamp;
}