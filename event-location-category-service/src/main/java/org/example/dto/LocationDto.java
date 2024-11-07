package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.model.Event;

import java.util.List;

@Data
public class LocationDto {

    private final Long id;

    @NotBlank(message = "slug cannot be blank")
    private final String slug;

    @NotBlank(message = "name cannot be blank")
    private final String name;

    private final List<Event> events;
}
