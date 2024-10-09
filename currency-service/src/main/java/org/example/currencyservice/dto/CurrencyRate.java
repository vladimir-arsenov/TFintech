package org.example.currencyservice.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrencyRate {

    @JsonAlias(value = "CharCode")
    private String currency;

    @JsonAlias(value = "Value")
    @JsonDeserialize()
    private float rate;

    public void setRate(String rate) {
        this.rate = Float.parseFloat(rate.replace(',', '.'));
    }
}