// Save aircraft on button click or display errors
function saveAircraft() {
    // Remove old highlighted errors
    removeErrorHighlights();

    if (validateAircraft()) {
        postEvent();
    }
}

function removeErrorHighlights() {
    let inputs = getInputs();
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].style.removeProperty("border-color");
    }
}

// Form Validation
function validateAircraft() {
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

// Post to /addAircraftEvent
function postEvent() {
    let inputs = getInputs();

    const aircraft = {
        tailNumber:     inputs[0].value.trim(),
        reason:         inputs[1].value.trim(),
        nextUpdate:     convertDateToSQL(inputs[2].value.trim()),
        remark:         inputs[3].value.trim(),
        backInService:  inputs[4].value.trim(),
        carrier: {
            carrierId:  inputs[5].value.trim()
        },
        startTime:      convertDateToSQL(inputs[6].value.trim())
    }

    $.ajax({
        type: "POST",
        url: "/addAircraftEvent",
        data: JSON.stringify(aircraft),
        contentType: "application/json",
        statusCode: {
            201: function() {
                // Reload page
                location.reload();
            },
            500: function() {
                // Display error
                displayResult("addAircraftEventAlert", "An error occurred saving the aircraft.");
            }
        }
    });
}

// Display error
function displayResult(alertBoxName, message) {
    const alert = document.getElementById(alertBoxName);
    alert.classList.remove("d-none");
    alert.innerText = message;

    // Hide alert after 5 seconds
    setTimeout(() => {
        if (!alert.classList.contains("d-none")) {
            alert.classList.add("d-none");
        }
    }, 5000);
}

// Get inputs
function getInputs() {
    // Have to manually do it this way or else it won't add to the array
    let inputs = [];
    let tailNumber = document.getElementById("tailNumber");
    let reason = document.getElementById("reason");
    let nextUpdate = document.getElementById("dtmNextUpdate");
    let remark = document.getElementById("strRemark");
    let serviceStatus = document.getElementById("serviceStatus");
    let carrier = document.getElementById("carrier");
    let startTime = document.getElementById("startTime");

    inputs.push(tailNumber);
    inputs.push(reason);
    inputs.push(nextUpdate);
    inputs.push(remark);
    inputs.push(serviceStatus);
    inputs.push(carrier);
    inputs.push(startTime);

    return inputs;
}

// Convert HTML date format to SQL
function convertDateToSQL(datetime) {
    // Convert local timezone to UTC
    let utcDate = new Date(datetime).toISOString();

    // Format date to match SQL
    let period = utcDate.indexOf(".");
    utcDate = utcDate.substring(0, period);
    utcDate = utcDate.replace("T", " ");

    return utcDate;
}