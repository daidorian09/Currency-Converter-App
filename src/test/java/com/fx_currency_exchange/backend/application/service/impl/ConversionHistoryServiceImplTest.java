package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.application.service.ConversionHistoryService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.service.ConversionTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConversionHistoryServiceImplTest {
    private ConversionTransactionRepository repository;
    private ConversionHistoryService service;

    @BeforeEach
    void setUp() {
        repository = mock(ConversionTransactionRepository.class);
        service = new ConversionHistoryServiceImpl(repository);
    }

    @Test
    void shouldReturnPagedConversionTransactions() {
        final UUID transactionId = UUID.randomUUID();
        final ConversationTransactionFilter filter = new ConversationTransactionFilter(transactionId, null, null);
        final Pageable pageable = PageRequest.of(0, 10);

        ConversionTransaction transaction = ConversionTransaction.builder()
                .id(transactionId)
                .fromCurrency("USD")
                .toCurrency("TRY")
                .amount(BigDecimal.valueOf(100))
                .convertedAmount(BigDecimal.valueOf(2700))
                .timestamp(LocalDateTime.now())
                .build();

        Page<ConversionTransaction> mockPage = new PageImpl<>(Collections.singletonList(transaction));

        when(repository.findByFilter(filter, pageable)).thenReturn(mockPage);

        Page<ConversionTransaction> result = service.findConversionTransactions(filter, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isOne();
        assertThat(result.getContent().get(0).getFromCurrency()).isEqualTo("USD");
        verify(repository, times(1)).findByFilter(filter, pageable);
    }
}