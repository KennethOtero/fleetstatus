// Get aircraft events on startup
getAircraftStatusTable();

// Show back in service on click
let tableBody = document.getElementById("statusDisplay");
tableBody.addEventListener("click", function(event) {
    if (event.target && event.target.id.startsWith("backInService-")) {
        const eventId = event.target.id.split("-")[1];
        showBackInService(eventId);
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
                <td>${events[i].nextUpdate}</td>
                <td>${events[i].remark}</td>
                <td>
                    <input type="checkbox" id="backInService-${eventId}"/>
                </td>
                <td>
                    <button class="btn btn-primary" onclick="${showEditModal(events[i])}">Update</button>
                </td>
            </tr>
        `;
    }
}