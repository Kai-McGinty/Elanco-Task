(() => {
  const speciesList = document.querySelector("#species-list");
  const status = document.querySelector("#species-status");

  fetch("/api/tick-species")
    .then((response) => {
      if (!response.ok) {
        throw new Error(`The species service returned an error (${response.status}).`);
      }
      return response.json();
    })
    .then((species) => {
      if (!Array.isArray(species)) {
        throw new Error("The species service returned an unexpected response.");
      }

      speciesList.replaceChildren();
      for (const entry of species) {
        const card = document.createElement("article");
        card.className = "species-card";
        const heading = document.createElement("header");
        heading.className = "species-heading";
        const commonName = document.createElement("h2");
        commonName.textContent = entry.commonName;
        const scientificName = document.createElement("p");
        scientificName.className = "scientific-name";
        scientificName.textContent = entry.scientificName;
        heading.append(commonName, scientificName);

        const facts = document.createElement("dl");
        facts.className = "species-facts";
        for (const [label, value] of [
          ["Appearance", entry.appearance],
          ["Size", entry.size],
          ["Habitat", entry.habitat],
          ["Risk", entry.risk]
        ]) {
          const fact = document.createElement("div");
          fact.className = "species-fact";
          const term = document.createElement("dt");
          term.textContent = label;
          const description = document.createElement("dd");
          description.textContent = value;
          fact.append(term, description);
          facts.append(fact);
        }

        card.append(heading, facts);
        speciesList.append(card);
      }

      status.textContent = species.length
        ? `${species.length} species in the sighting guide.`
        : "No species are currently available.";
    })
    .catch((error) => {
      status.textContent = error.message || "Could not load tick species.";
      status.classList.add("error");
    });
})();
