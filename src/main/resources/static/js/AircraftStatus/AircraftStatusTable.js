// Get aircraft events on startup
getAircraftStatusTable();

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
    let tableBody = document.getElementById("statusDisplay");

    for (let i = 0; i < aircraft.length; i++) {
        // Create a new row for each aircraft
        const row = document.createElement("tr");
        row.classList.add("text-white", "bg-dark");

        // Add fields
        const imageColumn = document.createElement("td");
        const image = document.createElement("img");
        image.alt = "aircraft status image";
        if (aircraft[i].backInService === 1) {
            image.src = "/images/SmallGreenAircraft.png";
        } else {
            image.src = "/images/SmallRedAircraft.png";
        }
        imageColumn.appendChild(image);

        const tailNumber = document.createElement("td");
        tailNumber.innerText = aircraft[i].tailNumber;

        const reason = document.createElement("td");
        let length = aircraft[i].reason.length;
        for (let j = 0; j < length; j++) {
            if (j + 1 < length) {
                reason.innerText += aircraft[i].reason[j].reason + ", ";
            } else {
                reason.innerText += aircraft[i].reason[j].reason;
            }
        }

        const nextUpdate = document.createElement("td");
        nextUpdate.innerText = aircraft[i].nextUpdate;

        const remark = document.createElement("td");
        remark.innerText = aircraft[i].remark;

        const backInServiceColumn = document.createElement("td");
        const backInService = document.createElement("input");
        backInService.type = "checkbox";
        backInService.id = "backInService-" + aircraft[i].aircraftId;
        backInService.onclick = function() {
            showBackInService(aircraft[i].aircraftId);
        };
        backInServiceColumn.appendChild(backInService);

        const downtime = document.createElement("td");
        downtime.id = "downtime-" + aircraft[i].aircraftId;
        downtime.innerText = updateDowntime(aircraft[i].aircraftId);

        // Add data to table
        row.append(imageColumn, tailNumber, reason, nextUpdate, remark, backInServiceColumn, downtime);
        tableBody.appendChild(row);
    }
}