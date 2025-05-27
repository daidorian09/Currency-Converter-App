package com.fx_currency_exchange.backend.infrastructure.persistence.jpa.spec;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ConversionTransactionEntity;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository.JpaConversionTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ConversationTransactionSpecificationTest {

    @Autowired
    private JpaConversionTransactionRepository repository;

    @Test
    void filterByTransactionId() {
        final UUID wantedId = UUID.randomUUID();
        saveTx(wantedId, LocalDateTime.now());
        saveTx(UUID.randomUUID(), LocalDateTime.now());

        final ConversationTransactionFilter filter =
                new ConversationTransactionFilter(wantedId, null, null);

        final Specification<ConversionTransactionEntity> spec =
                ConversationTransactionSpecification.byFilter(filter);

        final List<ConversionTransactionEntity> result = repository.findAll(spec);

        assertThat(result.size()).isOne();
        assertThat(result.get(0).getId()).isEqualTo(wantedId);
    }

    @Test
    void filterByStartDate() {
        final LocalDate today = LocalDate.now();
        final LocalDateTime yesterday = today.minusDays(1).atTime(10, 0);
        final LocalDateTime todayMorning = today.atStartOfDay().plusHours(8);

        saveTx(UUID.randomUUID(), yesterday);
        final UUID keptId = UUID.randomUUID();
        saveTx(keptId, todayMorning);

        final ConversationTransactionFilter filter =
                new ConversationTransactionFilter(null, today, null);

        final List<ConversionTransactionEntity> result =
                repository.findAll(ConversationTransactionSpecification.byFilter(filter));

        assertThat(result).extracting(ConversionTransactionEntity::getId)
                .containsExactly(keptId);
    }

    @Test
    void filterByFullRange() {
        final LocalDate start = LocalDate.now().minusDays(3);
        final LocalDate end = LocalDate.now().minusDays(1);

        saveTx(UUID.randomUUID(), start.minusDays(1).atTime(12, 0));
        saveTx(UUID.randomUUID(), end.plusDays(1).atTime(12, 0));

        final UUID keptId = UUID.randomUUID();
        saveTx(keptId, start.plusDays(1).atTime(9, 30));

        final ConversationTransactionFilter filter =
                new ConversationTransactionFilter(keptId, start, end);

        final List<ConversionTransactionEntity> result =
                repository.findAll(ConversationTransactionSpecification.byFilter(filter));

        assertThat(result).hasSize(1)
                .extracting(ConversionTransactionEntity::getId)
                .containsExactly(keptId);
    }

    private void saveTx(final UUID id, final LocalDateTime ts) {
        repository.save(
                ConversionTransactionEntity.builder()
                        .id(id)
                        .fromCurrency("USD")
                        .toCurrency("TRY")
                        .amount(BigDecimal.valueOf(100))
                        .convertedAmount(BigDecimal.valueOf(3200))
                        .timestamp(ts)
                        .build()
        );
    }
}