package com.Elanco.Tick_Sighting.BackEnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


@RestController 
public class TickController {

    private final TickService service;
    private final ObjectMapper objectMapper;
    private final UkLocationCoordinates coordinates;

    public TickController(TickService service, ObjectMapper objectMapper, UkLocationCoordinates coordinates) {
        this.service = service;
        this.objectMapper = objectMapper;
        this.coordinates = coordinates;
    }

    @GetMapping("/tick-sightings")
    public String getAllTickSightings() {
        return service.getTickSightings();
    }

    @GetMapping("/api/map/sightings")
    public List<MapSighting> getMapSightings() {
        JsonNode sightings = objectMapper.readTree(service.getTickSightings());
        if (!sightings.isArray()) {
            throw new InvalidTickSightingResponseException();
        }

        List<MapSighting> mapSightings = new ArrayList<>(sightings.size());
        for (JsonNode data : sightings) {
            TickSighting sighting = ParseJSON(data);
            UkLocationCoordinates.Coordinates location = coordinates.find(sighting.getLocation());
            mapSightings.add(new MapSighting(
                sighting.getId(),
                sighting.getLocation(),
                sighting.getDate(),
                sighting.getSpecies(),
                sighting.getLatinName(),
                location == null ? null : location.latitude(),
                location == null ? null : location.longitude()
            ));
        }

        return mapSightings;
    }

    @GetMapping("/api/tick-species")
    public List<TickSpeciesEntry> getTickSpecies() {
        return Arrays.stream(TickSpecies.values())
            .map(species -> new TickSpeciesEntry(
                species.getCommonName(),
                species.getScientificName(),
                species.getAppearance(),
                species.getSize(),
                species.getHabitat(),
                species.getRisk()
            ))
            .toList();
    }

    @GetMapping("/tick-sightings/{sightingId}")
    public TickSighting getSingleItem(@PathVariable String sightingId) {
        String TickJson = service.getSingleSighting(sightingId);
        return ParseJSON(objectMapper.readTree(TickJson));
    }
    
    @GetMapping("/tick-sightings/city/{city}")
    public List<TickSighting> getSightingByCity(@PathVariable String city) {
        JsonNode citySightings = objectMapper.readTree(service.getSightingByCity(city));

        List<TickSighting> parsedSightings = new ArrayList<>(citySightings.size());
        for (JsonNode sighting : citySightings) {
            parsedSightings.add(ParseJSON(sighting));
        }

    return parsedSightings;
    }

    @GetMapping("/tick-sightings/species")
    public List<TickSighting> getSightingBySpecies(@RequestParam String species) {
        JsonNode speciesSightings = objectMapper.readTree(service.getSightingBySpecies(species));

        List<TickSighting> parsedSpecies = new ArrayList<>(speciesSightings.size());
        for (JsonNode sighting : speciesSightings) {
            parsedSpecies.add(ParseJSON(sighting));
        }

    return parsedSpecies;
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
