package org.example.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClientException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest
class CentralBankClientTest {

    @Autowired
    private CentralBankClient centralBankClient;

    @BeforeAll
    public static void setUp() {
        configureFor(wireMockExtension.getPort());
    }

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicPort())
            .build();

    @DynamicPropertySource
    public static void setUpMockBaseUrl(DynamicPropertyRegistry registry) {
        registry.add("api.external.currenciesRate", () -> wireMockExtension.baseUrl() +"/scripts/XML_daily.asp");
    }

    @Test
    void getCurrencyRate_validXml_shouldReturnCurrencyRateList(){
        String xml = """
                <?xml version="1.0" encoding="windows-1251"?>
                <ValCurs Date="10.10.2024" name="Foreign Currency Market">
                    <Valute ID="R01010">
                        <NumCode>036</NumCode>
                        <CharCode>USD</CharCode>
                        <Nominal>1</Nominal>
                        <Name>Австралийский доллар</Name>
                        <Value>65,4013</Value>
                        <VunitRate>65,4013</VunitRate>
                    </Valute>
                    <Valute ID="R01020A">
                        <NumCode>944</NumCode>
                        <CharCode>AUD</CharCode>
                        <Nominal>1</Nominal>
                        <Name>Азербайджанский манат</Name>
                        <Value>57,0284</Value>
                        <VunitRate>57,0284</VunitRate>
                    </Valute>
                </ValCurs>
                """;
        stubFor(
                WireMock.get(urlEqualTo("/scripts/XML_daily.asp"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_XML_VALUE)
                                        .withBody(xml)
                        )
        );

        var rates = centralBankClient.getCurrencyRates();

        assertNotNull(rates);
        assertEquals(3, rates.size());
        assertTrue(rates.containsKey("USD"));
        assertTrue(rates.containsKey("RUB"));
        assertTrue(rates.containsKey("AUD"));
    }

    @Test
    void getCurrencyRates_tooManyRequests_shouldThrowRestClientException() {
        stubFor(
                get(urlEqualTo("/scripts/XML_daily.asp"))
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.TOO_MANY_REQUESTS.value())
                        )
        );

        assertThrows(RestClientException.class, () -> centralBankClient.getCurrencyRates());
    }
}