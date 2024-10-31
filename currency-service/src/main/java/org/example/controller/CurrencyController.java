package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.CurrencyConversionRequest;
import org.example.dto.CurrencyConversionResponse;
import org.example.dto.CurrencyRate;
import org.example.service.CurrencyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Currencies", description = "Controller for working with currencies")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;


    @Operation(summary = "Get a currency rate", description = "Returns a rate of the specified currency")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Currency code and rate are found",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrencyRate.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Currency doesn't exist",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(description = "Error description", example = "Currency doesn't exist: AAAAA")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service doesn't support this currency",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(description = "Error description", example = "Unsupported currency code: AAAAA")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service is temporarily unavailable",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(description = "Error description")
                            )
                    }
            )
    })
    @GetMapping("/rates/{code}")
    public CurrencyRate getRate(@Parameter(description = "Currency code", example = "USD") @PathVariable String code) {
        return currencyService.getRate(code);
    }

    @Operation(summary = "Convert money", description = "Converts a given amount of money to another currency")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Money are successfully converted",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CurrencyConversionResponse.class)
                            )
                    }),
            @ApiResponse(
                    responseCode = "400",
                    description = "Currency doesn't exist or incorrect request body",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(description = "Error description", example = "Currency doesn't exist: AAAAA")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Service doesn't support this currency",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(description = "Error description", example = "Unsupported currency code: AAAAA")
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Service is temporarily unavailable",
                    content = {
                            @Content(
                                    mediaType = "text/plain",
                                    schema = @Schema(description = "Error description")
                            )
                    }
            )
    })
    @PostMapping("/convert")
    public CurrencyConversionResponse convertCurrency(@Valid @RequestBody CurrencyConversionRequest conversionRequest) {
        return currencyService.convertCurrency(conversionRequest);
    }
}
