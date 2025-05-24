package com.fx_currency_exchange.backend.api.controller;

import com.fx_currency_exchange.backend.application.constant.ApplicationConstant;
import com.fx_currency_exchange.backend.application.dto.filter.ConversationTransactionFilter;
import com.fx_currency_exchange.backend.application.dto.response.ConversionHistoryResponse;
import com.fx_currency_exchange.backend.application.mapper.ConversionHistoryResponseMapper;
import com.fx_currency_exchange.backend.application.service.ConversionHistoryService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversion")
@RequiredArgsConstructor
public class ConversionController {

    private final ConversionHistoryService conversionHistoryService;

    @Operation(
            summary = "Retrieve conversion transaction history",
            description = "Returns a paginated list of conversion transactions filtered by transaction ID and/or date range.",
            parameters = {
                    @Parameter(name = "transactionId", description = "Filter by specific transaction ID", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                    @Parameter(name = "startTransactionDate", description = "Start date for filtering (inclusive)", example = "2025-05-01"),
                    @Parameter(name = "endTransactionDate", description = "End date for filtering (inclusive)", example = "2025-05-31"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10"),
                    @Parameter(name = "sort", description = "Sort by field and direction", example = "timestamp,desc")
            }
    )
    @GetMapping("/history")
    public ResponseEntity<Page<ConversionHistoryResponse>> getConversionHistory(
            @RequestParam(required = false) final UUID transactionId,
            @RequestParam(required = false) final LocalDate startTransactionDate,
            @RequestParam(required = false) final LocalDate endTransactionDate,
            @ParameterObject @PageableDefault(size = ApplicationConstant.DEFAULT_PAGE_SIZE,
                    sort = ApplicationConstant.DEFAULT_SORTING_FIELD,
                    direction = Sort.Direction.DESC) final Pageable pageable
    ) {
        final Page<ConversionTransaction> transactions = conversionHistoryService.
                findConversionTransactions(new ConversationTransactionFilter(transactionId, startTransactionDate, endTransactionDate), pageable);
        return ResponseEntity.ok(transactions.map(ConversionHistoryResponseMapper::from));
    }
}