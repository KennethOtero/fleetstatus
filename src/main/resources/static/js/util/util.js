/**
 * Endpoint constants
 */
// Aircraft endpoints
const URI_AIRCRAFT             = "/v1/aircraft";
const URI_CARRIER              = "/v1/carrier";
const URI_TYPE                 = "/v1/type";

// Event endpoints
const URI_EVENTS               = "/v1/events";
const URI_OOS_EVENTS           = "/v1/OutOfServiceEvents";
const URI_EVENT_HISTORY        = "/v1/EventHistory";
const URI_SHOW_BACK_IN_SERVICE = "/v1/showBackInService";
const URI_REASON               = "/v1/reason";
const URI_CSV                  = "/v1/csv";
const URI_DOWNTIME_REPORT      = "/v1/DownTimeReport";

// User endpoints
const URI_AUTH_STATUS          = "/v1/auth/status";

/**
 * Catch-all to convert both T-SQL DATETIME and ISO formats to datetime-local
 * @param inputDate
 * @returns {string}
 */
function convertToDateTimeLocal(inputDate) {
    let date;
    if (inputDate.includes(' ')) {
        // Assume T-SQL DATETIME format
        date = sqlToJsDate(inputDate);
    } else {
        // Assume ISO 8601 format
        date = parseIsoDate(inputDate);
    }
    return toDateTimeLocalString(date);
}

/**
 * Turn DATETIME to JavaScript Date object
 * @param sqlDate
 * @returns {Date}
 */
function sqlToJsDate(sqlDate) {
    // sqlDate is in the format "YYYY-MM-DD HH:MM:SS"
    const [datePart, timePart] = sqlDate.split(' ');
    const [year, month, day] = datePart.split('-');
    const [hours, minutes, seconds] = timePart.split(':');
    return new Date(year, month - 1, day, hours, minutes, seconds);
}

/**
 * Parses ISO date to JavaScript Date object
 * @param isoDate
 * @returns {Date}
 */
function parseIsoDate(isoDate) {
    return new Date(isoDate);
}

/**
 * Parse the Date object to a string to use in datetime-local input
 * @param date
 * @returns {string}
 */
function toDateTimeLocalString(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

/**
 * Format the nextUpdate DATETIME variable to MM-DD-YYYY HH:MMZ
 * @param date
 * @returns {null|string}
 */
function formatTime(date) {
    if (date === null || date === undefined) return null;

    const newDate = new Date(date);
    const year = newDate.getUTCFullYear();
    const month = String(newDate.getUTCMonth() + 1).padStart(2, '0');
    const day = String(newDate.getUTCDate()).padStart(2, '0');
    const hours = String(newDate.getUTCHours()).padStart(2, '0');
    const minutes = String(newDate.getUTCMinutes()).padStart(2, '0');

    return `${month}/${day}/${year} ${hours}:${minutes}Z`;
}

/**
 * Display error
 * @param alertBoxName
 * @param message
 */
function displayResult(alertBoxName, message) {
    const alert = document.getElementById(alertBoxName);
    alert.classList.remove("d-none");
    alert.innerText = message;

    // Hide alert after 5 seconds
    setTimeout(() => {
        if (!alert.classList.contains("d-none")) {
            alert.classList.add("d-none");
        }
    }, 5000);
}

/**
 * Convert HTML date format to SQL
 * @param datetime
 * @returns {string}
 */
function convertDateToSQL(datetime) {
    // Convert local timezone to UTC
    return new Date(datetime).toISOString();
}

/**
 * Get all aircraft
 */
function getAircraft(tailSelectId) {
    fetch(URI_AIRCRAFT)
        .then(response => response.json())
        .then(data => {
            const tailSelect = document.getElementById(tailSelectId);

            // Clear old options and add default
            addDefaultSelectOption(tailSelect, "Select Tail");

            data.forEach(aircraft => {
                const option = document.createElement("option");
                option.value = aircraft.aircraftId;
                option.text = aircraft.tailNumber;
                tailSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading aircraft:", error));
}

/**
 * Get all reasons
 */
function getReasons(reasonSelectId) {
    fetch(URI_REASON)
        .then(response => response.json())
        .then(data => {
            const reasonSelect = document.getElementById(reasonSelectId);

            // Clear old options and add default
            addDefaultSelectOption(reasonSelect, "Select Reason");

            data.forEach(reason => {
                const option = document.createElement("option");
                option.value = reason.reasonId;
                option.text = reason.reason;
                reasonSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading reasons:", error));
}

/**
 * Get all types
 */
function getTypes(typeSelectId) {
    fetch(URI_TYPE)
        .then(response => response.json())
        .then(data => {
            const typeSelect = document.getElementById(typeSelectId);

            // Clear old options and add default
            addDefaultSelectOption(typeSelect, "Select Type");

            data.forEach(type => {
                const option = document.createElement("option");
                option.value = type.typeId;
                option.text = type.typeName;
                typeSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading types:", error));
}

/**
 * Get all carriers
 */
function getCarriers(carrierSelectId) {
    fetch(URI_CARRIER)
        .then(response => response.json())
        .then(data => {
            const carrierSelect = document.getElementById(carrierSelectId);

            // Clear old options and add default
            addDefaultSelectOption(carrierSelect, "Select Carrier");

            data.forEach(carrier => {
                const option = document.createElement("option");
                option.value = carrier.carrierId;
                option.text = carrier.carrierName;
                carrierSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading carriers:", error));
}

function addDefaultSelectOption(element, text) {
    // Clear existing options
    element.innerText = "";

    // Add default option
    const defaultOption = document.createElement("option");
    defaultOption.value = "";
    defaultOption.text = text;
    element.appendChild(defaultOption);

    return element;
}