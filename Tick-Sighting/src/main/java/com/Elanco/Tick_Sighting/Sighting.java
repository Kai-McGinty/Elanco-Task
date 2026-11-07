package com.Elanco.Tick_Sighting;

import java.time.LocalDate;

public record Sighting(
		String id,
		LocalDate date,
		String location,
		String species,
		String latinName) {
}
