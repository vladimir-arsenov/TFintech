package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.CentralBankClient;
import org.example.dto.CurrencyConversionRequest;
import org.example.dto.CurrencyConversionResponse;
import org.example.dto.CurrencyRate;
import org.example.exception.NoSuchCurrencyException;
import org.example.exception.UnsupportedCurrencyException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Currency;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CurrencyService {

    private final CentralBankClient centralBankClient;

    public CurrencyRate getRate(String code) {

        log.info("Getting currency rate for {}", code);

        var rates = centralBankClient.getCurrencyRates();
        checkCurrency(code, rates);
        return rates.get(code);
    }


    public CurrencyConversionResponse convertCurrency(CurrencyConversionRequest conversionRequest) {
        log.info("Converting currency {}", conversionRequest);

        var rates = centralBankClient.getCurrencyRates();

        checkCurrency(conversionRequest.getFromCurrency(), rates);
        checkCurrency(conversionRequest.getToCurrency(), rates);

        var from = rates.get(conversionRequest.getFromCurrency());
        var to = rates.get(conversionRequest.getToCurrency());
        var amount = conversionRequest.getAmount();

        BigDecimal result = from.getRate().multiply(amount).divide(to.getRate(), MathContext.DECIMAL32);

        return new CurrencyConversionResponse(conversionRequest.getFromCurrency(), conversionRequest.getToCurrency(), result);
    }

    private void checkCurrency(String currencyCode, Map<String, CurrencyRate> centralBankRates) {
        try {
            Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new NoSuchCurrencyException("Currency doesn't exist: " + currencyCode);
        }
        if (!centralBankRates.containsKey(currencyCode)) {
            throw new UnsupportedCurrencyException("Unsupported currency code: " + currencyCode);
        }
    }


}
