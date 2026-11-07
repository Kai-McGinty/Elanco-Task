package com.Elanco.Tick_Sighting.BackEnd;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TickService {

    private final RestClient client;
    
    public TickService() {
        client = RestClient.builder()
        .baseUrl("https://dev-task.elancoapps.com/data/")
        .build();
    }

    public String getTickSightings() {
        return client.get().uri("/tick-sightings").retrieve().body(String.class);
    }

    public String getSingleSighting(String sightingId) {  
        return client.get().uri("/tick-sightings/{sightingId}", sightingId).retrieve().body(String.class);
    }

    public String getSightingByCity(String city) {
        return client.get().uri("/tick-sightings/city/{city}", city).retrieve().body(String.class);
    }

    public String getSightingBySpecies(String species) {
        return client.get().uri("/tick-sightings/species/{species}", species).retrieve().body(String.class);
    }
}
