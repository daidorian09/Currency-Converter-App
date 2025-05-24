package com.fx_currency_exchange.backend.application.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationConstant {
    public static final int CURRENCY_CODE_LENGTH = 3;
    public static final String EXCEPTION_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final String DEFAULT_SORTING_FIELD = "timestamp";
    public static final String CSV_FILE_DELIMITER = ",";
    public static final int EXPECTED_CSV_COLUMN_COUNT = 3;
    public static final String DOT = ".";
}