package com.fx_currency_exchange.backend.api.controller;

import com.fx_currency_exchange.backend.application.dto.response.FileUploadConversionResponse;
import com.fx_currency_exchange.backend.application.mapper.ConversionHistoryResponseMapper;
import com.fx_currency_exchange.backend.application.service.ConversionHistoryService;
import com.fx_currency_exchange.backend.application.service.FileUploadConversionService;
import com.fx_currency_exchange.backend.domain.entity.ConversionTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ConversionController.class)
@Import({ConversionHistoryResponseMapper.class})
class ConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversionHistoryService conversionHistoryService;

    @MockBean
    private FileUploadConversionService fileUploadConversionService;

    @Test
    void shouldReturnConversionHistory() throws Exception {
        final UUID transactionId = UUID.randomUUID();
        final ConversionTransaction tx = ConversionTransaction.builder()
                .id(transactionId)
                .fromCurrency("USD")
                .toCurrency("TRY")
                .amount(BigDecimal.valueOf(100))
                .convertedAmount(BigDecimal.valueOf(800))
                .timestamp(LocalDateTime.now())
                .build();

        final Page<ConversionTransaction> txPage = new PageImpl<>(List.of(tx));

        when(conversionHistoryService.findConversionTransactions(any(), any())).thenReturn(txPage);

        mockMvc.perform(get("/api/conversion/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].transactionId").value(transactionId.toString()))
                .andExpect(jsonPath("$.content[0].fromCurrency").value("USD"))
                .andExpect(jsonPath("$.content[0].toCurrency").value("TRY"));
    }

    @Test
    void shouldProcessUploadedCsvFile() throws Exception {
        final MockMultipartFile file = new MockMultipartFile(
                "file", "test.csv", "text/csv", "from,to,amount\nUSD,TRY,100".getBytes()
        );

        final FileUploadConversionResponse response = new FileUploadConversionResponse(1, 0);

        when(fileUploadConversionService.process(any())).thenReturn(response);

        mockMvc.perform(multipart("/api/conversion/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successCount").value(1))
                .andExpect(jsonPath("$.failureCount").value(0));
    }

    @Test
    void shouldReturnBadRequest_whenUploadCsvFails() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv", "invalid content".getBytes());

        when(fileUploadConversionService.process(any())).thenThrow(new RuntimeException("Parsing failed"));

        mockMvc.perform(multipart("/api/conversion/upload").file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("RuntimeException"))
                .andExpect(jsonPath("$.message").value("Parsing failed"));
    }
}