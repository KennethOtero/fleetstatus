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
    fetch("getOutOfServiceAircraft")
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

function displayAircraftStatusTable(aircraft) {
    if (aircraft.length === 0) {
        tableBody.innerHTML = `
        <tr class="text-white text-center bg-dark">
            <td colspan="7">No current events.</td>
        </tr>`;
        return;
    }

    for (let i = 0; i < aircraft.length; i++) {
        let aircraftId = aircraft[i].aircraftId;

        // Get correct aircraft image
        let imagePath = "";
        if (aircraft[i].backInService) {
            imagePath = "/images/SmallGreenAircraft.png";
        } else {
            imagePath = "/images/SmallRedAircraft.png";
        }

        // Get reasons
        let reasonString = "";
        let length = aircraft[i].reason.length;
        for (let j = 0; j < length; j++) {
            if (j + 1 < length) {
                reasonString += aircraft[i].reason[j].reason + ", ";
            } else {
                reasonString += aircraft[i].reason[j].reason;
            }
        }

        tableBody.innerHTML += `
            <tr class="text-white bg-dark">
                <td>
                    <img src="${imagePath}" alt="aircraft status image" />
                </td>
                <td>${aircraft[i].tailNumber}</td>
                <td>${reasonString}</td>
                <td>${aircraft[i].nextUpdate}</td>
                <td>${aircraft[i].remark}</td>
                <td>
                    <input type="checkbox" id="backInService-${aircraftId}"/>
                </td>
                <td id="downtime-${aircraftId}">${updateDowntime(aircraftId)}</td>
            </tr>
        `;
    }
}