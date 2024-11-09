package org.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.security.authentication.dto.AuthenticationRequest;
import org.example.security.authentication.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    public void register_validInput_shouldReturnToken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test", "test", false);
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void authenticate_registeredUser_shouldReturnOkStatus() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test1", "test1", false);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("test1", "test1", true);
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().isForbidden());

        var response = mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();

        String bearerTokenHeader = "Bearer " + objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(get("/api/v1/locations")
                        .header("Authorization", bearerTokenHeader))
                .andExpect(status().isOk());
    }

    @Test
    public void authenticate_nonExistingUser_shouldReturnForbiddenStatus() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test2", "test2", false);
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("nonExisting", "test2", true);
        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void logout_authenticatedUser_shouldInvalidateToken() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test3", "test3", false);
        var response = mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn().getResponse().getContentAsString();

        String bearerTokenHeader = "Bearer " + objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(get("/api/v1/locations")
                        .header("Authorization", bearerTokenHeader))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/logout")
                        .header("Authorization", bearerTokenHeader))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/locations")
                        .header("Authorization", bearerTokenHeader))
                .andExpect(status().isForbidden());
    }

    @Test
    public void changePassword_shouldChangePassword() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test4", "test4", false);
        var json = """
                {
                    "currentPassword" : "test4",
                    "newPassword" : "new",
                    "confirmationPassword" : "new"
                }""";
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("test4", "new", true);

        var response = mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andReturn().getResponse().getContentAsString();

        String bearerTokenHeader = "Bearer " + objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(patch("/api/v1/users/changePassword")
                        .header("Authorization", bearerTokenHeader)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk());
    }
}