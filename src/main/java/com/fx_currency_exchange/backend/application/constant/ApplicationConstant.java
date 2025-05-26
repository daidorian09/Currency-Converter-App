package com.fx_currency_exchange.backend.application.constant;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class ApplicationConstant {
    public static final String CURRENCY_CODE_REGEX = "^[A-Z]{3}$";
    public static final BigDecimal MINIMUM_CONVERSION_AMOUNT = BigDecimal.ZERO;
    public static final int ZERO = 0;
    public static final String EXCEPTION_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String DEFAULT_SORTING_FIELD = "timestamp";
    public static final String CSV_FILE_DELIMITER = ",";
    public static final int EXPECTED_CSV_COLUMN_COUNT = 3;
    public static final String DOT = ".";
    public static final int MAX_TOTAL_CONNECTION = 100;
    public static final int MAX_PER_ROUTE = 20;
    public static final int CONNECTION_TIMEOUT = 5000;
    public static final int READ_TIMEOUT = 5000;
    public static final int CONNECTION_REQUEST_TIMEOUT = 2000;
    public static final int IDLE_CONNECTION_EVICT_SECONDS = 30;
    public static final String EXCHANGE_SYNC_JOB_CRON_EXPRESSION = "0 0 * * * *";
    public static final int EXCHANGE_RATE_THREAD_POOL_SIZE = 1;
    public static final String EXCHANGE_RATE_SCHEDULED_JOB_PREFIX = "exchange-rate-sync-";
    public static final String CSV_UPLOAD_LOCK_KEY_PREFIX = "csv-upload-lock:";
    public static final int CSV_UPLOAD_LOCK_WAIT_TIME = 20;
    public static final int CSV_UPLOAD_LOCK_LEASE_TIME = 120;
}