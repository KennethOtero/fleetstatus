
var calendar; // Defining global variables

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

document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        height: 'auto',
        contentHeight: 500,
        aspectRatio: 2,
        eventOverlap: true,  // Allow multiple events on the same day
        slotEventOverlap: false,  // Prevent events from overlapping

        events: function(fetchInfo, successCallback, failureCallback) {
            fetch(URI_EVENT_HISTORY)
                .then(response => response.json())
                .then(events => {
                    const formattedEvents = events.map(event => ({
                        title: `${event.tailNumber} (${event.reason})`,
                        start: event.start,
                        end: event.end || event.start,
                        color: event.color || getRandomColor(),
                        display: 'block'
                    }));
                    successCallback(formattedEvents);
                })
                .catch(error => failureCallback(error));
        }
    });
    calendar.render();
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
    const urlTable = filterEventHistory(URI_EVENT_HISTORY);  // Tabular Data
    const urlCalendar = filterEventHistory(URI_CALENDER_EVENT_HISTORY);  // Calendar data

    // request information for table
    fetch(urlTable)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("statusDisplay");
            tableBody.innerHTML = '';  // Clear old data

            if (data.length === 0) {
                tableBody.innerHTML = `
                <tr class="text-white text-center bg-dark">
                    <td colspan="5">No current event history.</td>
                </tr>`;
                return;
            }

            displayEventHistory(data);  // Update table
        })
        .catch(error => console.log("Error fetching event history:", error));

    // request information for calender
    fetch(urlCalendar)
        .then(response => response.json())
        .then(data => {
            updateCalendarEvents(data);  // Update Calendar
        })
        .catch(error => console.log("Error fetching calendar events:", error));
}


function updateCalendarEvents(eventData) {
    // Clear existing calendar events
    calendar.getEvents().forEach(event => event.remove());

    // Re-add new events
    eventData.forEach(event => {
        calendar.addEvent({
            title: `${event.title}`,
            start: event.start,
            end: event.end || event.start,
            color: event.color,
            display: 'block'  // Fill the calendar grid with events
        });
    });
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