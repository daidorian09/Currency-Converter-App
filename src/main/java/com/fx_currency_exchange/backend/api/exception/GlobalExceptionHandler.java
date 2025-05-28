package com.fx_currency_exchange.backend.api.exception;

import com.fx_currency_exchange.backend.domain.exception.BaseFxCurrencyExchangeException;
import com.fx_currency_exchange.backend.domain.exception.ExchangeRateNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.fx_currency_exchange.backend.application.constant.ApplicationConstant.EXCEPTION_DATE_FORMAT;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(EXCEPTION_DATE_FORMAT);

    @ExceptionHandler(BaseFxCurrencyExchangeException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleDomainExceptions(final BaseFxCurrencyExchangeException ex) {
        log.warn("Exception occurred: {} - {}",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex);

        return ResponseEntity.badRequest().body(buildBody(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        ));
    }

    @ExceptionHandler(ExchangeRateNotFound.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleExchangeRateNotFoundException(final ExchangeRateNotFound ex) {
        log.warn("ExchangeRateNotFoundException occurred: {} - {}",
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildBody(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleGenericException(final Exception ex) {
        log.error("Unexpected exception occurred : ", ex);

        return ResponseEntity.internalServerError().body(buildBody(
                ex.getClass().getSimpleName(),
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        ));
    }

    private Map<String, Object> buildBody(final String error, final String message, final HttpStatus status) {
        return Map.of(
                "timestamp", FORMATTER.format(LocalDateTime.now()),
                "error", error,
                "message", message,
                "status", status.value()
        );
    }
}