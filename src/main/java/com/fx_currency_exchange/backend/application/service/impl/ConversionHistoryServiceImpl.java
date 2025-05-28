package com.fx_currency_exchange.backend.application.service.impl;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.application.service.ConversionHistoryService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.service.ConversionTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversionHistoryServiceImpl implements ConversionHistoryService {

    private final ConversionTransactionRepository repository;

    @Override
    public Page<ConversionTransaction> findConversionTransactions(final ConversationTransactionFilter filter, final Pageable pageable) {
        return repository.findByFilter(filter, pageable);
    }
}