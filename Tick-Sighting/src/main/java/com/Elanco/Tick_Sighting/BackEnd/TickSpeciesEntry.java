package com.Elanco.Tick_Sighting.BackEnd;

public record TickSpeciesEntry(
    String commonName,
    String scientificName,
    String appearance,
    String size,
    String habitat,
    String risk
) {
}
