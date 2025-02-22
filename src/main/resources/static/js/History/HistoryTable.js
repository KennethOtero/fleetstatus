window.onload = function() {
    loadCarriers();
    loadTypes();
    loadReasons();
    loadAircrafts();
};

// Loading carrier list
function loadCarriers() {
    fetch("/v1/getAllCarrier")
        .then(response => response.json())
        .then(data => {
            const carrierSelect = document.getElementById("carrierSelect");
            data.forEach(carrier => {
                const option = document.createElement("option");
                option.value = carrier.carrierId;
                option.text = carrier.carrierName;
                carrierSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading carriers:", error));
}

// Load Type List
function loadTypes() {
    fetch("/v1/getAllTypes")
        .then(response => response.json())
        .then(data => {
            const typeSelect = document.getElementById("typeSelect");
            data.forEach(type => {
                const option = document.createElement("option");
                option.value = type.typeId;
                option.text = type.typeName;
                typeSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading types:", error));
}

// Loading Reason List
function loadReasons() {
    fetch("/v1/getAllReason")
        .then(response => response.json())
        .then(data => {
            const reasonSelect = document.getElementById("reasonIds");
            data.forEach(reason => {
                const option = document.createElement("option");
                option.value = reason.reasonId;
                option.text = reason.reason;
                reasonSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading reasons:", error));
}

// Load aircraft list (tail numbers)
function loadAircrafts() {
    fetch("/v1/findAllAircraft")
        .then(response => response.json())
        .then(data => {
            const tailSelect = document.getElementById("tailSelect");
            data.forEach(aircraft => {
                const option = document.createElement("option");
                option.value = aircraft.tailNumber; //Preparing for fuzzy search
                option.text = aircraft.tailNumber;
                tailSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading aircrafts:", error));
}

function filterEventHistory(baseUrl){
    const carrierId = document.getElementById("carrierSelect").value;
    const typeId = document.getElementById("typeSelect").value;
    const tailNumber = document.getElementById("tailSelect").value;

    // Get the selected reason ID in the multi-select drop-down box
    const reasonSelect = document.getElementById("reasonIds");
    const selectedReasons = Array.from(reasonSelect.options)
        .filter(option => option.selected)
        .map(option => option.value);

    // Constructs a URL and adds selected query parameters to the URL
    const url = new URL(baseUrl, window.location.origin);

    if (carrierId) url.searchParams.append("carrierId", carrierId);
    if (typeId) url.searchParams.append("typeId", typeId);
    if (tailNumber) url.searchParams.append("tailNumber", tailNumber);
    selectedReasons.forEach(reasonId => {
        url.searchParams.append("reasonIds", reasonId);
    });
    return url;
}

function getEventHistory() {

    const url =filterEventHistory("/v1/getHistory");

    // Send a request to get data
    fetch(url)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("statusDisplay");
            tableBody.innerHTML = '';  // Clear previous data

            if (data.length === 0) {
                tableBody.innerHTML = `
                <tr class="text-white text-center bg-dark">
                    <td colspan="5">No current event history.</td>
                </tr>`;
                return;
            }

            displayEventHistory(data);
        })
        .catch(error => console.log("Error fetching event history:", error));
}

function displayEventHistory(events) {
    const tableBody = document.getElementById("statusDisplay");

    events.forEach(event => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${new Date(event.startTime).toLocaleDateString()}</td>
            <td>${event.aircraft.tailNumber}</td>
            <td>${event.reasonString}</td>
            <td>${event.remark}</td>
            <td>${formatTime(event.nextUpdate)}</td>
        `;
        tableBody.appendChild(row);
    });
}

function generateReport() {
    // Placeholder for generating report functionality
    alert("Generating aircraft down time report...");
}

function exportData() {
        const url = filterEventHistory('/v1/csv');
        const link = document.createElement('a');
        link.href = url.toString();  // The endpoint for exporting CSV
        link.click();
    alert("Exporting data...");
}