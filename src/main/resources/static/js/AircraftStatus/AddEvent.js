// Save event on button click or display errors
function saveEvent() {
    // Remove old highlighted errors
    removeErrorHighlights();

    if (validateEvent()) {
        createEvent();
    }
}

function removeErrorHighlights() {
    let inputs = getInputs();
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].style.removeProperty("border-color");
    }
}

// Form Validation
function validateEvent() {
    // Get inputs
    let inputs = getInputs();

    let result = true;

    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].value.trim().length === 0) {
            inputs[i].style.borderColor = "red";
            displayResult("addAircraftEventAlert", "One or more fields are empty.");
            result = false;
        }
    }

    return result;
}

function createEvent() {
    let inputs = getInputs();

    // Convert the selected reasonId to an array of Reason objects
    const selectedReasons = Array
        .from(inputs[1].selectedOptions)
        .map(option => {
        return {
            reasonId: option.value, // Map reasonId to Reason object
            reason: option.textContent
        };
    });

    const event = {
        aircraft: {
            // Add aircraftId once autocomplete feature is finished. Remove if statement on controller to save
            // aircraft once done.
            aircraftId: inputs[0].value.trim()
        },
        reason:         selectedReasons,
        nextUpdate:     convertDateToSQL(inputs[2].value.trim()),
        remark:         inputs[3].value.trim(),
        backInService:  0,
        startTime:      convertDateToSQL(inputs[4].value.trim())
    };

    $.ajax({
        type: "POST",
        url: URI_EVENTS,
        data: JSON.stringify(event),
        contentType: "application/json",
        statusCode: {
            201: function() {
                // Close modal and reload table
                let modalElement = document.getElementById("addTailEvent");
                let modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide();
                getAircraftStatusTable();
            },
            409: function() {
                displayResult("addAircraftEventAlert", "This tail number already exists.");
            },
            500: function() {
                // Display error
                displayResult("addAircraftEventAlert", "An error occurred saving the aircraft.");
            }
        }
    });
}

// Get inputs
function getInputs() {
    // Have to manually do it this way or else it won't add to the array
    let inputs = [];
    let tailNumber = document.getElementById("tailNumberSelect");
    let reason = document.getElementById("reason");
    let nextUpdate = document.getElementById("dtmNextUpdate");
    let remark = document.getElementById("strRemark");
    let startTime = document.getElementById("startTime");

    inputs.push(tailNumber);
    inputs.push(reason);
    inputs.push(nextUpdate);
    inputs.push(remark);
    inputs.push(startTime);

    return inputs;
}

// Get updated list of carriers, types, and reasons when modal is opened
$("#addTailEvent").on("show.bs.modal", () => {
    getReasons("reason");
    getAircraft("tailNumberSelect");
});