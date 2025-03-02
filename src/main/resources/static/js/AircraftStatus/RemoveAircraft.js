// Get updated list of aircraft when modal is opened
$("#removeAircraft").on("show.bs.modal", () => {
    getAircraft("deleteTail");
});

function removeAircraft() {
    if (validateRemove()) {
        deleteAircraft();
    }
}

function validateRemove() {
    let aircraftId = document.getElementById("deleteTail").value;

    if (aircraftId === "0" || aircraftId === "") {
        displayResult("removeAircraftAlert", "Please select a tail to remove.");
        return false;
    }

    return true;
}

function deleteAircraft() {
    let aircraftId = document.getElementById("deleteTail").value;

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