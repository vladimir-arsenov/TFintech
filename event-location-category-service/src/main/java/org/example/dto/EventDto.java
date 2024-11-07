package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDto {

    private final Long id;

    @NotBlank(message = "name cannot be blank")
    private final String name;

    @NotNull(message = "date cannot be null")
    private final LocalDate date;

    @NotNull(message = "locationId cannot be null")
    private final Long locationId;
}
