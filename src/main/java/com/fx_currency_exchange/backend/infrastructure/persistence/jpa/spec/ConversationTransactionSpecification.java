package com.fx_currency_exchange.backend.infrastructure.persistence.jpa.spec;

import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.infrastructure.persistence.jpa.entity.ConversionTransactionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class ConversationTransactionSpecification {

    private static final String ID_FIELD = "id";
    public static final String TIMESTAMP_FIELD = "timestamp";

    public static Specification<ConversionTransactionEntity> byFilter(final ConversationTransactionFilter filter) {
        return new GenericSpecificationBuilder<ConversionTransactionEntity>()
                .withCondition(Objects.nonNull(filter.transactionId()),
                        (root, cb) -> cb.equal(root.get(ID_FIELD), filter.transactionId()))

                .withCondition(Objects.nonNull(filter.startTransactionDate()),
                        (root, cb) -> cb.greaterThanOrEqualTo(root.get(TIMESTAMP_FIELD), filter.startTransactionDate().atStartOfDay()))

                .withCondition(Objects.nonNull(filter.endTransactionDate()),
                        (root, cb) -> cb.lessThanOrEqualTo(root.get(TIMESTAMP_FIELD), filter.endTransactionDate().plusDays(1).atStartOfDay()))

                .build();
    }
}
