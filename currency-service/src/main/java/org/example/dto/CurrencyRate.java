package org.example.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Currency rate entity")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrencyRate {

    @Schema(description = "Currency code", example = "USD")
    @JsonAlias(value = "CharCode")
    private String currency;

    @Schema(description = "Currency rate", example = "100.0")
    @JsonAlias(value = "Value")
    @JsonDeserialize()
    private BigDecimal rate;

    public void setRate(String rate) {
        this.rate = new BigDecimal(rate.replace(',', '.'));
    }
}