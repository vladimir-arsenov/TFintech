package org.example;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) {
        deserialize("src/main/resources/city-error.json");

        City city = deserialize("src/main/resources/city.json");
        toXml(city);
    }

    public static City deserialize(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        City city = null;
        try {
            city = objectMapper.readValue(new File(path), City.class);
            log.info("File's parsed to object {}", city.toString());
        } catch (JsonParseException e) {
            log.error("Error parsing file {}. {}", path, e.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return city;
    }

    public static void toXml(City city) {
        if (city == null) {
            log.error("Object City is null");
        }
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            xmlMapper.writeValue(new File("src/main/resources/city.xml"), city);
            log.info("XML file's created");
        } catch (IOException e) {
            log.error("Error writing xml: {}", e.getMessage());
        }
    }
}