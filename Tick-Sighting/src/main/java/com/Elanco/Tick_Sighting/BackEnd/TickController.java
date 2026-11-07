package com.Elanco.Tick_Sighting.BackEnd;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


@RestController 
public class TickController {

    private final TickService service;
    private final ObjectMapper objectMapper;

    public TickController(TickService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/tick-sightings")
    public String getAllTickSightings() {
        return service.getTickSightings();
    }

    @GetMapping("/tick-sightings/{sightingId}")
    public TickSighting getSingleItem(@PathVariable String sightingId) {
        String TickJson = service.getSingleSighting(sightingId);
        return ParseJSON(objectMapper.readTree(TickJson));
    }
    
    // Method to parse JSON and create a TickSighting object
    private TickSighting ParseJSON(JsonNode data) {
        JsonNode id = data.path("id");
        JsonNode location = data.path("location");
        JsonNode date = data.path("date");
        JsonNode species = data.path("species");
        JsonNode latinName = data.path("latinName");

        if (id.isMissingNode()
            || id.isNull()
            || location.isMissingNode()
            || location.isNull()
            || date.isMissingNode()
            || date.isNull()
            || species.isMissingNode()
            || species.isNull()
            || latinName.isMissingNode()
            || latinName.isNull()) {
            throw new InvalidTickSightingResponseException();
        }

        return new TickSighting(id.asString(),
            location.asString(),
            date.asString(),
            species.asString(),
            latinName.asString());
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    private static class InvalidTickSightingResponseException extends RuntimeException {
    }
}
