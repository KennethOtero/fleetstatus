// Get aircraft events on startup
getAircraftStatusTable();

// Show back in service on click
let tableBody = document.getElementById("statusDisplay");
tableBody.addEventListener("click", async function(event) {
    if (event.target && event.target.id.startsWith("backInService-")) {
        const eventId = event.target.id.split("-")[1];
        showBackInService(eventId);
    }

    // Open edit event modal on click
    if (event.target && event.target.id.startsWith("editEvent-")) {
        const eventId = event.target.id.split("-")[1];
        await editEvent(eventId);

        // Remove any highlighted text fields
        removeEditErrors();
    }
});

setInterval(() => {
    // Update aircraft every 10 seconds
    getAircraftStatusTable();
}, 10000);

// Update every 10 seconds
function getAircraftStatusTable() {
    fetch("/getOutOfServiceAircraft")
        .then(response => response.json())
        .then(data => {
            try {
                // Remove old data
                const tableBody = document.getElementById("statusDisplay");
                if (tableBody.hasChildNodes()) {
                    tableBody.innerHTML = '';
                }
                displayAircraftStatusTable(data);
            } catch(error) {
                console.log(error);
            }

        })
        .catch(error => console.log(error));
}

function displayAircraftStatusTable(events) {
    if (events.length === 0) {
        tableBody.innerHTML = `
        <tr class="text-white text-center bg-dark">
            <td colspan="7">No current events.</td>
        </tr>`;
        return;
    }

    for (let i = 0; i < events.length; i++) {
        let eventId = events[i].eventId;

        // Get correct aircraft image
        let imagePath = "";
        if (events[i].backInService) {
            imagePath = "/images/SmallGreenAircraft.png";
        } else {
            imagePath = "/images/SmallRedAircraft.png";
        }

        tableBody.innerHTML += `
            <tr class="text-white bg-dark">
                <td>
                    <img src="${imagePath}" alt="aircraft status image" />
                </td>
                <td>${events[i].aircraft.tailNumber}</td>
                <td>${events[i].reasonString}</td>
                <td>${formatZuluTime(events[i].nextUpdate)}</td>
                <td>${events[i].remark}</td>
                <td style="display: flex; align-items: center; gap: 10px;">
                    <!-- input type="checkbox" id="backInService-${eventId}"/ -->
                    <label for="backInServiceData" style="margin-right: 10px;">Back in Service Date:</label>
                    <input type="datetime-local" class="form-control" id="backInServiceDate" name="backInServiceDate">
                    <button id="updateBackInService" onclick="updateBackInService(${eventId})">Update</button>
                <td>
                    <button class="btn btn-primary" id="editEvent-${eventId}">Edit</button>
                </td>
            </tr>
        `;
    }
}

function updateBackInService(eventId) {
    const backInServiceDate = document.getElementById('backInServiceDate').value;

    if (!backInServiceDate) {
        alert('Please select a valid date.');
        return;
    }

    $.ajax({
        url: '/updateBackInService/' + eventId,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({ backInServiceDate: new Date(backInServiceDate).toISOString() }),
        success: function () {
            // Reload aircraft status table
            getAircraftStatusTable();
        },
        error: function (xhr, status, error) {
            console.error('Error updating back in service date:', error);
            alert('Failed to update back in service date.');
        }
    });
}