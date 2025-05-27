package com.fx_currency_exchange.backend.infrastructure.persistence.jpa;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ConversionTransactionEntity;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository.JpaConversionTransactionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConversionTransactionRepositoryImplTest {
    @Mock
    private JpaConversionTransactionRepository jpaRepository = mock(JpaConversionTransactionRepository.class);

    @InjectMocks
    private ConversionTransactionRepositoryImpl repository = new ConversionTransactionRepositoryImpl(jpaRepository);

    @Test
    void shouldFindByFilterAndMapToDomain() {
        final UUID id = UUID.randomUUID();
        final ConversionTransactionEntity entity = ConversionTransactionEntity.builder()
                .id(id)
                .fromCurrency("USD")
                .toCurrency("EUR")
                .amount(BigDecimal.valueOf(100))
                .convertedAmount(BigDecimal.valueOf(92.5))
                .timestamp(LocalDateTime.now())
                .build();

        final Page<ConversionTransactionEntity> entityPage = new PageImpl<>(List.of(entity));
        final Pageable pageable = PageRequest.of(0, 10);

        when(jpaRepository.findAll((Specification<ConversionTransactionEntity>) any(), eq(pageable))).thenReturn(entityPage);

        // Act
        final Page<ConversionTransaction> result = repository.findByFilter(new ConversationTransactionFilter(null, null, null), pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("USD", result.getContent().get(0).getFromCurrency());
    }

    @Test
    void shouldSaveTransactionEntity() {
        final ConversionTransaction transaction = ConversionTransaction.builder()
                .id(UUID.randomUUID())
                .fromCurrency("GBP")
                .toCurrency("TRY")
                .amount(BigDecimal.valueOf(50))
                .convertedAmount(BigDecimal.valueOf(2000))
                .timestamp(LocalDateTime.now())
                .build();

        repository.save(transaction);

        verify(jpaRepository).save(ArgumentMatchers.any(ConversionTransactionEntity.class));
    }
}