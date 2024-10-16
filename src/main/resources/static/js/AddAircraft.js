// Save aircraft on button click or display errors
function saveAircraft() {
    if (validateAircraft()) {
        postAircraft();
    }
}

// Form Validation
function validateAircraft() {
    // Get inputs
    let inputs = getInputs();

    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].length === 0) {
            displayResult("showAlert", "One or more fields are empty.");
            return false;
        }
    }

    return true;
}

// Post to /addAircraft
function postAircraft() {
    let inputs = getInputs();

    const aircraft = {
        tailNumber:     inputs[0],
        reason:         inputs[1],
        nextUpdate:     convertDateToSQL(inputs[2]),
        remark:         inputs[3],
        backInService:  inputs[4],
        carrier: {
            carrierId:  inputs[5]
        },
        startTime:      convertDateToSQL(inputs[6])
    }

    $.ajax({
        type: "POST",
        url: "/addAircraft",
        data: JSON.stringify(aircraft),
        contentType: "application/json",
        statusCode: {
            201: function() {
                // Reload page
                location.reload();
            },
            500: function() {
                // Display error
                displayResult("showAlert", "An error occurred saving the aircraft.");
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
    let tailNumber = document.getElementById("tailNumber").value.trim();
    let reason = document.getElementById("reason").value.trim();
    let nextUpdate = document.getElementById("dtmNextUpdate").value.trim();
    let remark = document.getElementById("strRemark").value.trim();
    let serviceStatus = document.getElementById("serviceStatus").value.trim();
    let carrier = document.getElementById("carrier").value.trim();
    let startTime = document.getElementById("startTime").value.trim();

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