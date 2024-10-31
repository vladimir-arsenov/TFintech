package org.example.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CurrencyRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Component
public class CentralBankClient {

    @Value("${api.external.currenciesRate}")
    private String currencyRatesUrl;

    private final RestTemplate restTemplate;

    @Cacheable("currenciesRates")
    @CircuitBreaker(name = "centralBankClient", fallbackMethod = "fallbackAfterCircuitBreaker")
    public Map<String, CurrencyRate> getCurrencyRates(){
        return parseRates(restTemplate.getForEntity(currencyRatesUrl, String.class).getBody());
    }

    private Map<String, CurrencyRate> parseRates(String xml) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        log.info("Acquiring currency rates from {}", currencyRatesUrl);

        Map<String, CurrencyRate> map = null;
        try {
            map = xmlMapper.readValue(xml, new TypeReference<List<CurrencyRate>>() {})
                    .stream()
                    .collect(Collectors.toMap(CurrencyRate::getCurrency, a -> a));
        } catch (JsonProcessingException e) {
            log.error("Error parsing currency rates from xml: {}", e.getMessage());
        }
        map.remove(null);
        map.put("RUB", new CurrencyRate("RUB", new BigDecimal("1.0")));

        log.info("Acquired currency rates");

        return map;
    }

    private Map<String, CurrencyRate> fallbackAfterCircuitBreaker(Exception e) {
        log.warn("Fallback method for CircuitBreaker started: {}", e.getMessage());
        return new HashMap<>();
    }

}
