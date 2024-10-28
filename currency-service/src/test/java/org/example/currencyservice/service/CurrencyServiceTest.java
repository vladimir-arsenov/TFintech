package org.example.currencyservice.service;


import org.example.currencyservice.client.CentralBankClient;
import org.example.currencyservice.dto.CurrencyConversionRequest;
import org.example.currencyservice.dto.CurrencyRate;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrencyServiceTest {

    @Test
    public void convertCurrency_shouldConvertProperly() {
        var rates = Map.of("USD", new CurrencyRate("USD", 90.0f),
                "RUB", new CurrencyRate("RUB", 1f),
                "EUR", new CurrencyRate("EUR", 100.0f));
        var conversionRequest1 = new CurrencyConversionRequest( "USD", "RUB", 10f);
        var conversionRequest2 = new CurrencyConversionRequest( "USD", "EUR", 10f);
        var clientMock = mock(CentralBankClient.class);
        when(clientMock.getCurrencyRates()).thenReturn(rates);
        var service = new CurrencyService(clientMock);

        assertEquals(service.convertCurrency(conversionRequest1).getConvertedAmount(), 900f);
        assertEquals(service.convertCurrency(conversionRequest2).getConvertedAmount(), 9f);
    }
}
