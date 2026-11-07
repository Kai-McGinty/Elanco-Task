package com.Elanco.Tick_Sighting.BackEnd;

public class TickSighting {
    private final String id;
    private String date;
    private String location;
    private String species;
    private String latinName;

    public TickSighting(String id, String location, String date, String species, String latinName) {
        this.id = id;
        this.location = location;
        this.date = date;
        this.species = species;
        this.latinName = latinName;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }
}
