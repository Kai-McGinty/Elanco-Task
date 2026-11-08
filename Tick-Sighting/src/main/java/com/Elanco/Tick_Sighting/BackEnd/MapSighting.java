package com.Elanco.Tick_Sighting.BackEnd;

public record MapSighting(
    String id,
    String location,
    String date,
    String species,
    String latinName,
    Double latitude,
    Double longitude
) {
}
