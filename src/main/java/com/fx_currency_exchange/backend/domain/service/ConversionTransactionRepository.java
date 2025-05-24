package com.fx_currency_exchange.backend.domain.service;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConversionTransactionRepository {
    Page<ConversionTransaction> findByFilter(final ConversationTransactionFilter filter, final Pageable pageable);

    void save(final ConversionTransaction transaction);
}
