// Get aircraft events on startup
getAircraftStatusTable();

// Show back in service on click
let tableBody = document.getElementById("statusDisplay");
tableBody.addEventListener("click", function(event) {
    if (event.target && event.target.id.startsWith("backInService-")) {
        const aircraftId = event.target.id.split("-")[1];
        showBackInService(aircraftId);
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

function displayAircraftStatusTable(event) {
    if (event.length === 0) {
        tableBody.innerHTML = `
        <tr class="text-white text-center bg-dark">
            <td colspan="7">No current events.</td>
        </tr>`;
        return;
    }

    for (let i = 0; i < event.length; i++) {
        let eventId = event[i].eventId;

        // Get correct aircraft image
        let imagePath = "";
        if (event[i].backInService) {
            imagePath = "/images/SmallGreenAircraft.png";
        } else {
            imagePath = "/images/SmallRedAircraft.png";
        }

        // Get reasons
        let reasonString = "";
        let length = event[i].reason.length;
        for (let j = 0; j < length; j++) {
            if (j + 1 < length) {
                reasonString += event[i].reason[j].reason + ", ";
            } else {
                reasonString += event[i].reason[j].reason;
            }
        }

        tableBody.innerHTML += `
            <tr class="text-white bg-dark">
                <td>
                    <img src="${imagePath}" alt="aircraft status image" />
                </td>
                <td>${event[i].aircraft.tailNumber}</td>
                <td>${reasonString}</td>
                <td>${event[i].nextUpdate}</td>
                <td>${event[i].remark}</td>
                <td>
                    <input type="checkbox" id="backInService-${eventId}"/>
                </td>
                <td id="downtime-${eventId}">${updateDowntime(eventId)}</td>
            </tr>
        `;
    }
}