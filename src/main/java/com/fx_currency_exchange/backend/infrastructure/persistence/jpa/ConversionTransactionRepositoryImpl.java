package com.fx_currency_exchange.backend.infrastructure.persistence.jpa;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import com.fx_currency_exchange.backend.domain.service.ConversionTransactionRepository;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.repository.JpaConversionTransactionRepository;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.spec.ConversationTransactionSpecification;
import com.fx_currency_exchange.backend.infrastructure.persistence.mapper.ConversionTransactionMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConversionTransactionRepositoryImpl implements ConversionTransactionRepository {

    private final JpaConversionTransactionRepository jpaRepository;

    @Override
    public Page<ConversionTransaction> findByFilter(final ConversationTransactionFilter filter, final Pageable pageable) {
        return jpaRepository
                .findAll(ConversationTransactionSpecification.byFilter(filter), pageable)
                .map(ConversionTransactionMapper::toDomain);
    }

    @Override
    @Transactional
    public void save(final ConversionTransaction transaction) {
        jpaRepository.save(ConversionTransactionMapper.toEntity(transaction));
    }
}