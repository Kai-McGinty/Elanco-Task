package com.Elanco.Tick_Sighting.BackEnd;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class UkLocationCoordinates {

    private final Map<String, Coordinates> locations = Map.ofEntries(
        Map.entry("Birmingham", new Coordinates(52.4862, -1.8904)),
        Map.entry("Bristol", new Coordinates(51.4545, -2.5879)),
        Map.entry("Cardiff", new Coordinates(51.4816, -3.1791)),
        Map.entry("Edinburgh", new Coordinates(55.9533, -3.1883)),
        Map.entry("Glasgow", new Coordinates(55.8642, -4.2518)),
        Map.entry("Leeds", new Coordinates(53.8008, -1.5491)),
        Map.entry("Leicester", new Coordinates(52.6369, -1.1398)),
        Map.entry("Liverpool", new Coordinates(53.4084, -2.9916)),
        Map.entry("London", new Coordinates(51.5072, -0.1276)),
        Map.entry("Manchester", new Coordinates(53.4808, -2.2426)),
        Map.entry("Newcastle", new Coordinates(54.9783, -1.6178)),
        Map.entry("Nottingham", new Coordinates(52.9548, -1.1581)),
        Map.entry("Sheffield", new Coordinates(53.3811, -1.4701)),
        Map.entry("Southampton", new Coordinates(50.9097, -1.4044))
    );

    public Coordinates find(String location) {
        return locations.get(location);
    }

    public record Coordinates(double latitude, double longitude) {
    }
}
