package org.example.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.configuration.ApplicationStartUpListener;
import org.example.controller.CategoryController;
import org.example.model.Category;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import java.util.NoSuchElementException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CategoryControllerIntegrationTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryController categoryController;

    @Autowired
    private ApplicationStartUpListener applicationStartUpListener;

    @Container
    static WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:3.6.0");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("api.url.categories", () -> wiremockServer.getBaseUrl() + "/public-api/v1.4/categories");
        registry.add("api.url.categories", () -> wiremockServer.getBaseUrl() + "/public-api/v1.4/place-categories");
    }

    @BeforeAll
    public static void beforeAll() {
        wiremockServer.start();
        configureFor(wiremockServer.getPort());
    }

    @BeforeEach
    public void setUp() throws JsonProcessingException {

        Category[] categories = { new Category(1, "", " "), new Category(2, "", " "),
                new Category(3, "", " "), new Category(4, "", " ")};
        String json = objectMapper.writeValueAsString(categories);
        stubFor(
                WireMock.get(urlEqualTo("/public-api/v1.4/place-categories"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody(json)
                        )
        );
        applicationStartUpListener.initRepositories();
    }

    @Test
    public void shouldReturnCategories() throws JsonProcessingException {
        var result = categoryController.getCategories();

        assertFalse(result.isEmpty());
    }

    @Test
    public void shouldGetCategory(){
        var category = categoryController.getCategory(1);

        assertEquals(1, category.getId());
    }

    @Test
    public void shouldAddCategory() {
        var newCategory = new Category(5, "", " ");
        categoryController.addCategory(newCategory);

        var result = categoryController.getCategory(5);

        assertEquals(newCategory, result);
    }

    @Test
    public void shouldUpdateCategory() {
        categoryController.updateCategory(new Category(5, "", "updated"));

        var result = categoryController.getCategory(5);

        assertEquals("updated", result.getName());
    }

    @Test
    public void shouldDeleteCategory() {
        categoryController.deleteCategory(5);

        assertThrows(NoSuchElementException.class, () -> categoryController.getCategory(5));
    }
}
