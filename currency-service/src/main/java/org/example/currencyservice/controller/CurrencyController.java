package org.example.currencyservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.currencyservice.dto.CurrencyConversionRequest;
import org.example.currencyservice.dto.CurrencyConversionResponse;
import org.example.currencyservice.dto.CurrencyRate;
import org.example.currencyservice.service.CurrencyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;


    @GetMapping("/rates/{code}")
    public CurrencyRate getRate(@PathVariable String code) {
        return currencyService.getRate(code);
    }

    @PostMapping("/convert")
    public CurrencyConversionResponse convertCurrency(@Valid @RequestBody CurrencyConversionRequest conversionRequest) {
        return currencyService.convertCurrency(conversionRequest);
    }
}
