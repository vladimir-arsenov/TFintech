package org.example.currencyservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CurrencyConversionResponse {
    private String fromCurrency;
    private String toCurrency;
    private Float convertedAmount;
}
