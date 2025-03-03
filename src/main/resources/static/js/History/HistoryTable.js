document.addEventListener("DOMContentLoaded", function() {
    // Get elements
    const startTimeInput = document.getElementById("startTime");
    const endTimeInput = document.getElementById("endTime");

    // Set the default value to today
    const now = new Date();
    const formattedDate = now.toISOString().slice(0, 16); // 'yyyy-MM-ddTHH:mm'
    startTimeInput.value = formattedDate;
    endTimeInput.value = formattedDate;
});


window.onload = function() {
    getCarriers("carrierSelect");
    getTypes("typeSelect");
    getReasons("reasonIds");
    getEventHistoryAircraft();
};

function getEventHistoryAircraft() {
    fetch(URI_AIRCRAFT)
        .then(response => response.json())
        .then(data => {
            const tailSelect = document.getElementById("tailSelect");

            // Clear old options and add default
            addDefaultSelectOption(tailSelect, "Select Tail");

            data.forEach(aircraft => {
                const option = document.createElement("option");
                option.value = aircraft.tailNumber; //Preparing for fuzzy search
                option.text = aircraft.tailNumber;
                tailSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading aircraft:", error));
}

function filterEventHistory(baseUrl){
    const carrierId = document.getElementById("carrierSelect").value;
    const typeId = document.getElementById("typeSelect").value;
    const tailNumber = document.getElementById("tailSelect").value;

    const startTime = document.getElementById("startTime").value;
    const endTime = document.getElementById("endTime").value;

    // Get the selected reason ID in the multi-select drop-down box
    const reasonSelect = document.getElementById("reasonIds");
    const selectedReasons = Array.from(reasonSelect.options)
        .filter(option => option.selected)
        .map(option => option.value);

    const startTimeDate = new Date(startTime);
    const endTimeDate = new Date(endTime);
    if (startTimeDate > endTimeDate) {
        alert("Start time cannot be later than end time.");
        return null;  // If time is invalid, return null to stop the request
    }

    // Constructs a URL and adds selected query parameters to the URL
    const url = new URL(baseUrl, window.location.origin);

    if (carrierId) url.searchParams.append("carrierId", carrierId);
    if (typeId) url.searchParams.append("typeId", typeId);
    if (tailNumber) url.searchParams.append("tailNumber", tailNumber);
    selectedReasons.forEach(reasonId => {
        url.searchParams.append("reasonIds", reasonId);
    });

    if (startTime) url.searchParams.append("startDate", startTime);
    if (endTime) url.searchParams.append("endDate", endTime);

    return url;
}

function getEventHistory() {

    const url = filterEventHistory(URI_EVENT_HISTORY);

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
            <td>${event.downTime}</td>
        `;
        tableBody.appendChild(row);
    });
}

function exportData() {
    const url = filterEventHistory(URI_CSV);
    const link = document.createElement('a');
    link.href = url.toString();  // The endpoint for exporting CSV
    link.click();
}

function exportDowntimeReport() {
    const url = filterEventHistory(URI_DOWNTIME_REPORT);
    const link = document.createElement('a');
    link.href = url.toString();  // The endpoint for exporting CSV
    link.click();
}