function removeAircraft() {
    if (validateRemove()) {
        postRemoveAircraft();
    }
}

function validateRemove() {
    let aircraftId = document.getElementById("allTails").value;

    if (aircraftId === "0") {
        displayResult("removeAlert", "Please select a tail to remove.");
        return false;
    }

    return true;
}

function postRemoveAircraft() {
    let aircraftId = document.getElementById("allTails").value;

    $.ajax({
        type: "POST",
        url: "/removeAircraft",
        data: aircraftId,
        contentType: "application/json",
        statusCode: {
            200: function() {
                // Reload page
                location.reload();
            },
            500: function() {
                // Display error
                displayResult("removeAlert", "An error occurred deleting the aircraft.");
            }
        }
    });
}