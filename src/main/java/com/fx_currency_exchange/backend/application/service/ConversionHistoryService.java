package com.fx_currency_exchange.backend.application.service;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConversionHistoryService {
    Page<ConversionTransaction> findConversionTransactions(final ConversationTransactionFilter filter, final Pageable pageable);
}
