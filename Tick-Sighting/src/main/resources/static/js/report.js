(() => {
  const speciesSelect = document.querySelector("#species");
  const locationSelect = document.querySelector("#location");
  const status = document.querySelector("#options-status");
  const dateTimeInput = document.querySelector("#date-time");
  const dateTimeError = document.querySelector("#date-time-error");

  function getLocalDateTimeValue(date) {
    const pad = (value) => String(value).padStart(2, "0");
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
  }

  function validateDateTime() {
    const now = getLocalDateTimeValue(new Date());
    dateTimeInput.max = now;
    const isFuture = dateTimeInput.value > now;
    const message = isFuture ? "Choose a date and time that is not in the future." : "";
    dateTimeInput.setCustomValidity(message);
    dateTimeError.textContent = message;
  }

  function populateSelect(select, placeholder, options) {
    select.replaceChildren();
    const placeholderOption = document.createElement("option");
    placeholderOption.value = "";
    placeholderOption.textContent = placeholder;
    select.append(placeholderOption);

    for (const value of options) {
      const option = document.createElement("option");
      option.value = value;
      option.textContent = value;
      select.append(option);
    }
    select.disabled = options.length === 0;
  }

  validateDateTime();
  dateTimeInput.addEventListener("input", validateDateTime);
  dateTimeInput.addEventListener("change", validateDateTime);
  dateTimeInput.addEventListener("focus", validateDateTime);
  window.setInterval(validateDateTime, 30000);

  fetch("/api/report/options")
    .then((response) => {
      if (!response.ok) {
        throw new Error(`The sightings service returned an error (${response.status}).`);
      }
      return response.json();
    })
    .then((options) => {
      if (!Array.isArray(options.species) || !Array.isArray(options.locations)) {
        throw new Error("The sightings service returned invalid species or location options.");
      }
      populateSelect(speciesSelect, "Select a species", options.species);
      populateSelect(locationSelect, "Select a location", options.locations);
      status.textContent = `Loaded ${options.species.length} species and ${options.locations.length} locations.`;
    })
    .catch((error) => {
      status.textContent = error.message || "Could not load species and location options.";
      status.classList.add("error");
      speciesSelect.disabled = true;
      locationSelect.disabled = true;
      speciesSelect.replaceChildren(new Option("Species unavailable", ""));
      locationSelect.replaceChildren(new Option("Locations unavailable", ""));
    });
})();
