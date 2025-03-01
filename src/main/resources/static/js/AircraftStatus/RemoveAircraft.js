// Get updated list of aircraft when modal is opened
$("#removeAircraft").on("show.bs.modal", () => {
    fetchAircraft("allTails");
});

function removeAircraft() {
    if (validateRemove()) {
        postRemoveAircraft();
    }
}

function validateRemove() {
    let aircraftId = document.getElementById("allTails").value;

    if (aircraftId === "0") {
        displayResult("removeAircraftAlert", "Please select a tail to remove.");
        return false;
    }

    return true;
}

function postRemoveAircraft() {
    let aircraftId = document.getElementById("allTails").value;

    $.ajax({
        type: "DELETE",
        url: URI_AIRCRAFT + "?aircraftId=" + aircraftId,
        statusCode: {
            200: function() {
                // Close modal and reload table
                let modalElement = document.getElementById("removeAircraft");
                let modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide();
                getAircraftStatusTable();
            },
            500: function() {
                // Display error
                displayResult("removeAircraftAlert", "An error occurred deleting the aircraft.");
            }
        }
    });
}

function fetchAircraft(selectId) {
    fetch(URI_AIRCRAFT)
        .then(response => response.json())
        .then(data => {
            const allAircraft = document.getElementById(selectId);

            // Clear old results
            allAircraft.innerHTML = "";

            // Add default choice
            const defaultOption = document.createElement("option");
            defaultOption.value = 0;
            defaultOption.textContent = "Select Tail";
            allAircraft.appendChild(defaultOption);

            data.forEach(aircraft => {
                const option = document.createElement('option');
                option.value = aircraft.aircraftId;
                option.textContent = aircraft.tailNumber;
                allAircraft.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching aircraft:', error));
}