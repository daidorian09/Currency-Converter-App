package com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository;

import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ExchangeRateEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaExchangeRateRepositoryTest {

    @Autowired
    private JpaExchangeRateRepository repository;

    @Test
    void returnsExactMatch_whenPairExists() {
        // given
        repository.saveAll(
                java.util.List.of(
                        stub("EUR", 0.93),
                        stub("GBP", 0.79)
                )
        );

        Optional<ExchangeRateEntity> usdEur =
                repository.findByFromCurrencyAndToCurrency("USD", "EUR");

        assertThat(usdEur)
                .isPresent()
                .get()
                .satisfies(e -> {
                    assertThat(e.getFromCurrency()).isEqualTo("USD");
                    assertThat(e.getToCurrency()).isEqualTo("EUR");
                    assertThat(e.getRate()).isEqualByComparingTo("0.93");
                });
    }

    @Test
    void returnsEmpty_whenPairDoesNotExist() {
        repository.save(stub("CHF", 0.89));

        assertThat(
                repository.findByFromCurrencyAndToCurrency("EUR", "USD")
        ).isNotPresent();
    }

    private ExchangeRateEntity stub(final String to, final double rate) {
        return ExchangeRateEntity.builder()
                .id(UUID.randomUUID())
                .fromCurrency("USD")
                .toCurrency(to)
                .rate(BigDecimal.valueOf(rate))
                .build();
    }
}