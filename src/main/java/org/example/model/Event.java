package org.example.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Event {
    private final int id;
    private final String title;
    private final BigDecimal price;

    public Event(int id, String title, String price) {
        this.id = id;
        this.title = title;
        Matcher matcher = Pattern.compile("\\d+").matcher(price);
        this.price = new BigDecimal(matcher.find() ? matcher.group() : "0");
    }
}
