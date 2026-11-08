(() => {
  const mapElement = document.querySelector("#sightings-map");
  const status = document.querySelector("#map-status");
  const speciesFilter = document.querySelector("#species-filter");
  const dateFrom = document.querySelector("#date-from");
  const dateTo = document.querySelector("#date-to");
  const legend = document.querySelector("#map-legend");
  const speciesColors = [
    "#245c43",
    "#3478a5",
    "#a35b22",
    "#7851a9",
    "#b13e52",
    "#52616b"
  ];
  let sightings = [];
  let markers;

  function showStatus(message, isError = false) {
    status.textContent = message;
    status.classList.toggle("error", isError);
  }

  function dateKey(value) {
    const text = String(value || "");
    const match = text.match(/^(\d{4}-\d{2}-\d{2})/);
    if (match && !Number.isNaN(Date.parse(`${match[1]}T00:00:00Z`))) {
      return match[1];
    }

    const parsedDate = new Date(text);
    if (Number.isNaN(parsedDate.getTime())) {
      return null;
    }
    return parsedDate.toISOString().slice(0, 10);
  }

  function getSpeciesColor(species) {
    const speciesIndex = [...new Set(sightings.map((sighting) => sighting.species))]
      .sort()
      .indexOf(species);
    return speciesColors[Math.max(speciesIndex, 0) % speciesColors.length];
  }

  function addLegend(speciesList) {
    legend.replaceChildren();
    for (const species of speciesList) {
      const item = document.createElement("span");
      item.className = "legend-item";
      const dot = document.createElement("span");
      dot.className = "legend-dot";
      dot.style.backgroundColor = getSpeciesColor(species);
      const label = document.createElement("span");
      label.textContent = species;
      item.append(dot, label);
      legend.append(item);
    }

    const recentItem = document.createElement("span");
    recentItem.className = "legend-item";
    const recentRing = document.createElement("span");
    recentRing.className = "recent-indicator";
    const recentLabel = document.createElement("span");
    recentLabel.textContent = "Reported in the last 12 months";
    recentItem.append(recentRing, recentLabel);
    legend.append(recentItem);
  }

  function populateSpeciesFilter(speciesList) {
    for (const species of speciesList) {
      const option = document.createElement("option");
      option.value = species;
      option.textContent = species;
      speciesFilter.append(option);
    }
  }

  function createPopup(location) {
    const popup = document.createElement("div");
    const titleRow = document.createElement("div");
    titleRow.className = "popup-title-row";
    const title = document.createElement("h3");
    title.className = "popup-title";
    title.textContent = location.name;
    const shareButton = document.createElement("button");
    shareButton.type = "button";
    shareButton.className = "share-button";
    shareButton.setAttribute("aria-label", `Copy ${location.name} tick report summary`);
    shareButton.textContent = "Share";
    shareButton.addEventListener("click", async (event) => {
      event.stopPropagation();
      const reportLines = Object.entries(location.speciesCounts)
        .sort((a, b) => b[1] - a[1] || a[0].localeCompare(b[0]))
        .map(([species, count]) => `${species} - ${count} ${count === 1 ? "report" : "reports"}`);
      const clipboardText = [location.name, ...reportLines].join("\n");

      try {
        if (!navigator.clipboard?.writeText) {
          throw new Error("Clipboard access is unavailable in this browser or page context.");
        }
        await navigator.clipboard.writeText(clipboardText);
        shareButton.textContent = "Copied";
        showStatus(`Copied ${location.name} report summary to the clipboard.`);
      } catch (error) {
        showStatus(error.message || "Could not copy the report summary to the clipboard.", true);
      }
    });
    titleRow.append(title, shareButton);
    const summary = document.createElement("p");
    summary.className = "popup-summary";
    summary.textContent = `${location.count} ${location.count === 1 ? "report" : "reports"}`;
    const list = document.createElement("ul");
    list.className = "popup-species";
    for (const [species, count] of Object.entries(location.speciesCounts)
      .sort((a, b) => b[1] - a[1])) {
      const item = document.createElement("li");
      item.textContent = `${species}: ${count}`;
      list.append(item);
    }
    popup.append(titleRow, summary, list);

    if (location.latestDate) {
      const latest = document.createElement("p");
      latest.className = "popup-summary";
      latest.textContent = `Latest report: ${location.latestDate}`;
      popup.append(latest);
    }
    if (location.recentCount > 0) {
      const recent = document.createElement("p");
      recent.className = "popup-recent";
      recent.textContent = `${location.recentCount} report${location.recentCount === 1 ? "" : "s"} in the last 12 months`;
      popup.append(recent);
    }
    return popup;
  }

  function renderSightings() {
    const start = dateFrom.value;
    const end = dateTo.value;
    if (start && end && start > end) {
      showStatus("The end date must be on or after the start date.", true);
      return;
    }

    const filteredSightings = sightings.filter((sighting) => {
      if (speciesFilter.value && sighting.species !== speciesFilter.value) {
        return false;
      }
      if (!start && !end) {
        return true;
      }
      const sightingDate = dateKey(sighting.date);
      return sightingDate !== null
        && (!start || sightingDate >= start)
        && (!end || sightingDate <= end);
    });

    const recentCutoff = new Date();
    recentCutoff.setDate(recentCutoff.getDate() - 365);
    const recentCutoffKey = recentCutoff.toISOString().slice(0, 10);
    const locations = new Map();
    let unmappedCount = 0;

    for (const sighting of filteredSightings) {
      if (!Number.isFinite(sighting.latitude) || !Number.isFinite(sighting.longitude)) {
        unmappedCount += 1;
        continue;
      }

      let location = locations.get(sighting.location);
      if (!location) {
        location = {
          name: sighting.location,
          latitude: sighting.latitude,
          longitude: sighting.longitude,
          count: 0,
          speciesCounts: {},
          latestDate: null,
          recentCount: 0
        };
        locations.set(sighting.location, location);
      }

      location.count += 1;
      location.speciesCounts[sighting.species] = (location.speciesCounts[sighting.species] || 0) + 1;
      const key = dateKey(sighting.date);
      if (key && (!location.latestDate || key > location.latestDate)) {
        location.latestDate = key;
      }
      if (key && key >= recentCutoffKey) {
        location.recentCount += 1;
      }
    }

    markers.clearLayers();
    for (const location of locations.values()) {
      const dominantSpecies = Object.entries(location.speciesCounts)
        .sort((a, b) => b[1] - a[1])[0][0];
      const radius = Math.min(28, 8 + Math.sqrt(location.count) * 2.5);
      L.circleMarker([location.latitude, location.longitude], {
        radius,
        color: location.recentCount ? "#194632" : "#ffffff",
        weight: location.recentCount ? 4 : 2,
        fillColor: getSpeciesColor(dominantSpecies),
        fillOpacity: 0.82
      }).bindPopup(createPopup(location)).addTo(markers);
    }

    if (locations.size === 0) {
      showStatus(`No sightings matched the selected filters.${unmappedCount ? ` ${unmappedCount} matching reports have no mapped location.` : ""}`);
      return;
    }

    const mappedCount = [...locations.values()].reduce((total, location) => total + location.count, 0);
    const unmappedNote = unmappedCount
      ? ` ${unmappedCount} reports have locations that could not be mapped.`
      : "";
    showStatus(`Showing ${mappedCount} of ${filteredSightings.length} matching reports across ${locations.size} locations.${unmappedNote}`);
  }

  if (typeof L === "undefined") {
    showStatus("The map library could not be loaded. Check your connection and reload the page.", true);
    return;
  }

  const map = L.map(mapElement, { scrollWheelZoom: false }).setView([54.4, -2.2], 6);
  markers = L.layerGroup();
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    maxZoom: 18,
    attribution: "&copy; OpenStreetMap contributors"
  }).addTo(map);
  markers.addTo(map);

  fetch("/api/map/sightings")
    .then((response) => {
      if (!response.ok) {
        throw new Error(`The sightings service returned an error (${response.status}).`);
      }
      return response.json();
    })
    .then((data) => {
      if (!Array.isArray(data)) {
        throw new Error("The sightings service returned an unexpected response.");
      }
      sightings = data;
      const speciesList = [...new Set(sightings.map((sighting) => sighting.species))].sort();
      populateSpeciesFilter(speciesList);
      addLegend(speciesList);
      renderSightings();
    })
    .catch((error) => {
      showStatus(error.message || "Could not load sightings for the map.", true);
    });

  for (const filter of [speciesFilter, dateFrom, dateTo]) {
    filter.addEventListener("change", renderSightings);
  }
})();
