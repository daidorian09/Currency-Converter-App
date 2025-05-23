package com.fx_currency_exchange.backend.application.dto.filter;

import java.time.LocalDate;
import java.util.UUID;

public record ConversationTransactionFilter(UUID transactionId, LocalDate startTransactionDate, LocalDate endTransactionDate) {
}
