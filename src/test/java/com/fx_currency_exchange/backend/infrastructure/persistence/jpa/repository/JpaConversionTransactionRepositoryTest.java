package com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository;

import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ConversionTransactionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaConversionTransactionRepositoryTest {
    @Autowired
    private JpaConversionTransactionRepository repository;

    @Test
    void whenSave_thenEntityIsPersistedAndRetrievable() {
        final UUID id = UUID.randomUUID();
        final ConversionTransactionEntity tx = ConversionTransactionEntity.builder()
                .id(id)
                .fromCurrency("USD")
                .toCurrency("EUR")
                .amount(BigDecimal.valueOf(100))
                .convertedAmount(BigDecimal.valueOf(0.93))
                .build();

        repository.save(tx);

        // then
        assertThat(repository.findById(id))
                .isPresent()
                .get()
                .extracting(ConversionTransactionEntity::getFromCurrency)
                .isEqualTo("USD");
    }

    @Test
    void whenFindAllWithSpecification_thenFilteredResultReturned() {
        // seed a small data set
        repository.save(tx("USD", "EUR", 100));
        repository.save(tx("USD", "GBP", 200));
        repository.save(tx("TRY", "EUR", 300));

        final Specification<ConversionTransactionEntity> usdOnly =
                (root, q, cb) -> cb.equal(root.get("fromCurrency"), "USD");

        final List<ConversionTransactionEntity> result = repository.findAll(usdOnly);

        assertThat(result)
                .hasSize(2)
                .allMatch(e -> "USD".equals(e.getFromCurrency()));
    }

    private ConversionTransactionEntity tx(final String from, final String to, final double amount) {
        return ConversionTransactionEntity.builder()
                .id(UUID.randomUUID())
                .fromCurrency(from)
                .toCurrency(to)
                .amount(BigDecimal.valueOf(amount))
                .convertedAmount(BigDecimal.ONE)
                .build();
    }
}