package com.fx_currency_exchange.backend.infrastructure.persistence.jpa;

import com.fx_currency_exchange.backend.domain.entity.ExchangeRate;
import com.fx_currency_exchange.backend.domain.service.ExchangeRateRepository;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository.JpaExchangeRateRepository;
import com.fx_currency_exchange.backend.infrastructure.persistence.mapper.ExchangeRateMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {

    private final JpaExchangeRateRepository jpaRepository;

    @Override
    public Optional<ExchangeRate> findByCurrencyPair(final String from, final String to) {
        return jpaRepository.findByFromCurrencyAndToCurrency(from, to)
                .map(ExchangeRateMapper::toDomain);
    }

    @Override
    @Transactional
    public void save(final ExchangeRate exchangeRate) {
        jpaRepository.save(ExchangeRateMapper.toEntity(exchangeRate));
    }
}