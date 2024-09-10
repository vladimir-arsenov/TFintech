package org.example;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class City {
    String slug;
    @JsonProperty("coords")
    Coordinates coordinates;
}

@Data
class Coordinates {

    @JsonProperty("lat")
    double latitude;

    @JsonProperty("lon")
    double longitude;
}
