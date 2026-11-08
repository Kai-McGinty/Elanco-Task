package com.Elanco.Tick_Sighting.BackEnd;

public enum TickSpecies {
    MARSH_TICK(
        "Marsh tick",
        "Ixodes apronophorus",
        "Small, hard-bodied tick; appearance varies with life stage and feeding.",
        "A few millimetres as an unfed adult; larvae and nymphs are smaller, and feeding increases size.",
        "Prefers wetland habitats.",
        "parasitizes water vole and, historically, coypu."
    ),
    TREE_HOLE_TICK(
        "Tree-hole tick",
        "Ixodes arboricola",
        "Small, hard-bodied tick; appearance varies with life stage and feeding.",
        "A few millimetres as an unfed adult; larvae and nymphs are smaller, and feeding increases size.",
        "Favours tree-hole nesting birds such as Great Tit and Blue Tit.",
        "Great Tit and Blue Tit."
    ),
    SOUTHERN_RODENT_TICK(
        "Southern rodent tick",
        "Ixodes acuminatus",
        "Small, hard-bodied tick; appearance varies with life stage and feeding.",
        "A few millimetres as an unfed adult; larvae and nymphs are smaller, and feeding increases size.",
        "Inhabits burrows of small rodents.",
        "A sighting does not establish that a tick carries infection. UKHSA does not routinely test submitted ticks for pathogens."
    ),
    FOX_BADGER_TICK(
        "Fox/badger tick",
        "Ixodes canisuga",
        "Hard-bodied tick; appearance varies with life stage and feeding.",
        "A few millimetres as an unfed adult; larvae and nymphs are smaller, and feeding increases size.",
        "Widely distributed throughout the UK.",
        "Commonly found on badgers, foxes and domestic dogs."
    ),
    PASSERINE_TICK(
        "Passerine tick",
        "Dermacentor frontalis",
        "Hard-bodied tick; appearance varies with life stage and feeding.",
        "A few millimetres as an unfed adult; larvae and nymphs are smaller, and feeding increases size.",
        "Solely an ectoparasite of birds, particularly passerines.",
        "Responsible for avian tick-related haemorrhagic syndrome."
    );

    private final String commonName;
    private final String scientificName;
    private final String appearance;
    private final String size;
    private final String habitat;
    private final String risk;

    TickSpecies(
        String commonName,
        String scientificName,
        String appearance,
        String size,
        String habitat,
        String risk
    ) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.appearance = appearance;
        this.size = size;
        this.habitat = habitat;
        this.risk = risk;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getAppearance() {
        return appearance;
    }

    public String getSize() {
        return size;
    }

    public String getHabitat() {
        return habitat;
    }

    public String getRisk() {
        return risk;
    }
}
