package com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository;

import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ExchangeRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaExchangeRateRepository extends JpaRepository<ExchangeRateEntity, UUID> {
    Optional<ExchangeRateEntity> findByFromCurrencyAndToCurrency(final String from, final String to);
}