package org.example.service;


import org.example.client.CentralBankClient;
import org.example.dto.CurrencyConversionRequest;
import org.example.dto.CurrencyRate;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrencyServiceTest {

    @Test
    public void convertCurrency_shouldConvertProperly() {
        var rates = Map.of("USD", new CurrencyRate("USD", new BigDecimal("90")),
                "RUB", new CurrencyRate("RUB", new BigDecimal("1")),
                "EUR", new CurrencyRate("EUR", new BigDecimal("100")));
        var conversionRequest1 = new CurrencyConversionRequest( "USD", "RUB", new BigDecimal("10"));
        var conversionRequest2 = new CurrencyConversionRequest( "USD", "EUR", new BigDecimal("10"));
        var clientMock = mock(CentralBankClient.class);
        when(clientMock.getCurrencyRates()).thenReturn(rates);
        var service = new CurrencyService(clientMock);

        assertEquals(service.convertCurrency(conversionRequest1).getConvertedAmount(), new BigDecimal("900"));
        assertEquals(service.convertCurrency(conversionRequest2).getConvertedAmount(),  new BigDecimal("9"));
    }
}
