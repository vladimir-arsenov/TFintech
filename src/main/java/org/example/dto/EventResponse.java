package org.example.dto;

import lombok.Data;
import org.example.model.Event;

@Data
public class EventResponse {
    private Event[] results;
}
