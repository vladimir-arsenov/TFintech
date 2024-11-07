package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "Currency conversion request body")
@AllArgsConstructor
@Data
public class CurrencyConversionRequest {
    @Schema(description = "Currency to convert from", example = "USD")
    @NotBlank(message = "fromCurrency is mandatory")
    private String fromCurrency;

    @Schema(description = "Currency to convert to", example = "EUR")
    @NotBlank(message = "toCurrency is mandatory")
    private String toCurrency;

    @Schema(description = "Amount of money to convert", example = "15.24")
    @NotNull(message = "amount is mandatory")
    @Min(value = 0, message = "amount can't be less than zero")
    private BigDecimal amount;
}
