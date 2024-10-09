package org.example.currencyservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CurrencyConversionRequest {
    @NotBlank(message = "fromCurrency is mandatory")
    private String fromCurrency;

    @NotBlank(message = "toCurrency is mandatory")
    private String toCurrency;

    @NotNull(message = "amount is mandatory")
    @Min(value = 0, message = "amount can't be less than zero")
    private Float amount;
}
