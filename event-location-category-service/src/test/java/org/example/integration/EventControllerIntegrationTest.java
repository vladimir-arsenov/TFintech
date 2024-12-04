package org.example.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.EventDto;
import org.example.model.Event;
import org.example.model.Location;
import org.example.repository.EventRepository;
import org.example.repository.LocationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
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
        eventRepository.deleteAll();
        locationRepository.deleteAll();
    }

    @Test
    void addEvent_shouldAddEventToRepository() throws Exception {
        var location = new Location(1L, "test", "Test Location", Collections.emptyList());
        var savedLocation = locationRepository.save(location);
        var eventDto = new EventDto(0L, "Test Event", LocalDate.now(), savedLocation.getId());

        mockMvc.perform(post("/api/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isOk());
        var savedEvent = eventRepository.findAll().stream()
                .filter(event -> event.getName().equals("Test Event"))
                .findFirst();

        assertTrue(savedEvent.isPresent());
    }

    @Test
    void getEvent_shouldReturnEventFromRepository() throws Exception {
        var location = new Location(1L, "test", "Test Location", Collections.emptyList());
        var savedLocation = locationRepository.save(location);
        var event = new Event();
        event.setName("Existing Event");
        event.setDate(LocalDate.now());
        event.setLocation(savedLocation);
        event = eventRepository.save(event);

        mockMvc.perform(get("/api/v1/events/" + event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Existing Event")))
                .andExpect(jsonPath("$.locationId", is(savedLocation.getId().intValue())));
    }

    @Test
    void updateEvent_shouldUpdateEventInRepository() throws Exception {
        var location = new Location(1L, "test", "Test Location", Collections.emptyList());
        var savedLocation = locationRepository.save(location);
        var event = new Event();
        event.setName("Original Event");
        event.setDate(LocalDate.now());
        event.setLocation(savedLocation);
        event = eventRepository.save(event);

        EventDto updatedEventDto = new EventDto(0L, "Updated Event", LocalDate.now().plusDays(1), savedLocation.getId());

        mockMvc.perform(put("/api/v1/events/" + event.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEventDto)))
                .andExpect(status().isOk());

        var updatedEvent = eventRepository.findById(event.getId()).orElseThrow();
        assertTrue(updatedEvent.getName().equals("Updated Event"));
    }

    @Test
    void deleteEvent_shouldDeleteEventFromRepository() throws Exception {
        var location = new Location(1L, "test", "Test Location", Collections.emptyList());
        var savedLocation = locationRepository.save(location);
        var event = new Event();
        event.setName("Event to Delete");
        event.setDate(LocalDate.now());
        event.setLocation(savedLocation);
        event = eventRepository.save(event);

        mockMvc.perform(delete("/api/v1/events/" + event.getId()))
                .andExpect(status().isOk());

        var deletedEvent = eventRepository.findById(event.getId());
        assertTrue(deletedEvent.isEmpty());
    }

    @Test
    void getAllEvents_shouldGetAllEventFromRepository() throws Exception {
        var location = new Location(1L, "test", "Test Location", Collections.emptyList());
        var savedLocation = locationRepository.save(location);
        var event1 = new Event();
        event1.setName("Event 1");
        event1.setDate(LocalDate.now());
        event1.setLocation(savedLocation);
        eventRepository.save(event1);
        var event2 = new Event();
        event2.setName("Event 2");
        event2.setDate(LocalDate.now().plusDays(1));
        event2.setLocation(savedLocation);
        eventRepository.save(event2);

        mockMvc.perform(get("/api/v1/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Event 1")))
                .andExpect(jsonPath("$[1].name", is("Event 2")));
    }
}
