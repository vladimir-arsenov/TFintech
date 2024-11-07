package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "Result of currency conversion")
@AllArgsConstructor
@Data
public class CurrencyConversionResponse {
    @Schema(description = "Currency from which result was converted", example = "USD")
    private String fromCurrency;
    @Schema(description = "Currency that result was converted into", example = "RUB")
    private String toCurrency;
    @Schema(description = "Amount of money after conversion", example = "213.312")
    private BigDecimal convertedAmount;
}
