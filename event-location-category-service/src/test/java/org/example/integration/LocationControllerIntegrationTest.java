package org.example.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.LocationDto;
import org.example.model.Location;
import org.example.repository.LocationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(roles = {"ADMIN"})
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class LocationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocationRepository locationRepository;

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

    @BeforeAll
    static void setUp() {
        postgreSQLContainer.start();
    }

    @BeforeEach
    void cleanDatabase() {
        locationRepository.deleteAll();
    }

    @Test
    void addLocation_shouldAddLocationToRepository() throws Exception {
        LocationDto locationDto = new LocationDto(null, "new-location", "New Location", Collections.emptyList());

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationRepository, times(1)).save(locationCaptor.capture());
        assertEquals("New Location", locationCaptor.getValue().getName());
        assertEquals("new-location", locationCaptor.getValue().getSlug());
    }

    @Test
    void addLocation_invalidRequest_shouldReturnBadRequest() throws Exception {
        LocationDto locationDto = new LocationDto(null, "", "", Collections.emptyList());

        mockMvc.perform(post("/api/v1/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(locationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateLocation_shouldUpdateLocationInRepository() throws Exception {
        var location = new Location();
        location.setName("Original Location");
        location.setSlug("original-location");
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        LocationDto updatedLocationDto = new LocationDto(null, "updated-location", "Updated Location", Collections.emptyList());

        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLocationDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationRepository, times(1)).save(locationCaptor.capture());
        assertEquals("Updated Location", locationCaptor.getValue().getName());
        assertEquals("updated-location", locationCaptor.getValue().getSlug());
    }

    @Test
    void updateLocation_invalidRequest_shouldReturnBadRequest() throws Exception {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        LocationDto updatedLocationDto = new LocationDto(null, "updated-location", "Updated Location", Collections.emptyList());

        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedLocationDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteLocation_shouldDeleteLocationFromRepository() throws Exception {
        var location = new Location();
        location.setName("Location to Delete");
        location.setSlug("location-to-delete");
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));

        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isOk());

        ArgumentCaptor<Location> locationCaptor = ArgumentCaptor.forClass(Location.class);
        verify(locationRepository, times(1)).delete(locationCaptor.capture());
        assertEquals("Location to Delete", locationCaptor.getValue().getName());
    }

    @Test
    void deleteLocation_invalidRequest_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().isBadRequest());
    }
}
