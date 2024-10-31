package org.example.controller;

import org.example.dto.CurrencyConversionRequest;
import org.example.dto.CurrencyConversionResponse;
import org.example.dto.CurrencyRate;
import org.example.exception.NoSuchCurrencyException;
import org.example.exception.UnsupportedCurrencyException;
import org.example.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyService currencyService;

    @Test
    void getRate_validCurrencyCode_shouldReturnOkResponseWithCurrencyRate() throws Exception {
        when(currencyService.getRate(anyString())).thenReturn(new CurrencyRate("USD", new BigDecimal("73.5")));

        mockMvc.perform(get("/currencies/rates/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.rate").value(73.5));
    }

    @Test
    void convertCurrency_validRequest_shouldReturnOkResponseWithConvertedCurrency() throws Exception {
        CurrencyConversionResponse response = new CurrencyConversionResponse("USD", "EUR", new BigDecimal("85.0"));
        when(currencyService.convertCurrency(any(CurrencyConversionRequest.class))).thenReturn(response);
        String requestBody = """
            {
                "fromCurrency": "USD",
                "toCurrency": "EUR",
                "amount": 100
            }
            """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCurrency").value(response.getFromCurrency()))
                .andExpect(jsonPath("$.toCurrency").value(response.getToCurrency()))
                .andExpect(jsonPath("$.convertedAmount").value(response.getConvertedAmount()));
    }

    @Test
    void convertCurrency_notFoundCurrencyCode_shouldReturnBadRequestResponse() throws Exception {
        when(currencyService.convertCurrency(any())).thenThrow(new UnsupportedCurrencyException("Unsupported currency code: AAAA"));

        String requestBody = """
            {
                "fromCurrency": "AAAA",
                "toCurrency": "EUR",
                "amount": 100
            }
            """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Unsupported currency code: AAAA"));
    }

    @Test
    void getRate_notFoundCurrencyCode_shouldReturnNotFoundResponse() throws Exception {
        when(currencyService.getRate(anyString())).thenThrow(new UnsupportedCurrencyException("Unsupported currency code: AAAA"));

        mockMvc.perform(get("/currencies/rates/AAAA"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Unsupported currency code: AAAA"));
    }

    @Test
    void convertCurrency_nonExistingCurrencyCode_shouldReturnBadRequestResponse() throws Exception {
        when(currencyService.convertCurrency(any())).thenThrow(new NoSuchCurrencyException("Currency doesn't exist: AAAA"));

        String requestBody = """
            {
                "fromCurrency": "AAAA",
                "toCurrency": "EUR",
                "amount": 100
            }
            """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Currency doesn't exist: AAAA"));
    }

    @Test
    void getRate_nonExistingCurrencyCode_shouldReturnBadRequestResponse() throws Exception {
        when(currencyService.getRate(anyString())).thenThrow(new NoSuchCurrencyException("Currency doesn't exist: AAAA"));

        mockMvc.perform(get("/currencies/rates/AAAA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Currency doesn't exist: AAAA"));
    }


    @Test
    void convertCurrency_invalidRequestBody_shouldReturnBadRequestResponse() throws Exception {
        String invalidRequest = """
            {
                "fromCurrency": "",
                "amount": -12
            }
            """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"amount\":\"amount can't be less than zero\",\"toCurrency\":\"toCurrency is mandatory\",\"fromCurrency\":\"fromCurrency is mandatory\"}"));
    }

    @Test
    void getRate_missingPathVariable_shouldReturnBadRequestResponse() throws Exception {
        mockMvc.perform(get("/currencies/rates/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void convertCurrency_serviceUnavailable_shouldReturnServiceUnavailableResponse() throws Exception {
        when(currencyService.convertCurrency(any())).thenThrow(new RestClientException(""));
        String requestBody = """
            {
                "fromCurrency": "USD",
                "toCurrency": "EUR",
                "amount": 100
            }
            """;

        mockMvc.perform(post("/currencies/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isServiceUnavailable())
                .andExpect(MockMvcResultMatchers.header().string("Retry-After", "60"));
    }

    @Test
    void getRate_serviceUnavailable_shouldReturnServiceUnavailableResponse() throws Exception {
        when(currencyService.getRate(any())).thenThrow(new RestClientException(""));

        mockMvc.perform(get("/currencies/rates/USD"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(MockMvcResultMatchers.header().string("Retry-After", "60"));
    }
}