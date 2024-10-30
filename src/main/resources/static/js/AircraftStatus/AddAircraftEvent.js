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

    // Convert the selected reasonId to an array of Reason objects
    const selectedReasons = Array
        .from(inputs[1].selectedOptions)
        .map(option => {
        return {
            reasonId: option.value, // Map reasonId to Reason object
            reason: option.textContent
        };
    });

    console.log("Selected Reasons:", selectedReasons);

    const event = {
        aircraft: {
            // Add aircraftId once autocomplete feature is finished. Remove if statement on controller to save
            // aircraft once done.
            tailNumber: inputs[0].value.trim(),
            carrier: {
                carrierId: inputs[4].value.trim()
            },
            type: {
                typeId: inputs[6].value.trim()
            }
        },
        reason:         selectedReasons,
        nextUpdate:     convertDateToSQL(inputs[2].value.trim()),
        remark:         inputs[3].value.trim(),
        backInService:  0,
        startTime:      convertDateToSQL(inputs[5].value.trim())
    };

    $.ajax({
        type: "POST",
        url: "/saveEvent",
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
    let carrier = document.getElementById("carrier");
    let startTime = document.getElementById("startTime");
    let type = document.getElementById("addEventAircraftType");

    inputs.push(tailNumber);
    inputs.push(reason);
    inputs.push(nextUpdate);
    inputs.push(remark);
    inputs.push(carrier);
    inputs.push(startTime);
    inputs.push(type);

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

// Get updated list of carriers, types, and reasons when modal is opened
$("#addTailEvent").on("show.bs.modal", () => {
    fetchReasons();
    fetchCarriers();
    fetchTypes();
});

function fetchReasons() {
    fetch('/getAllReason')
        .then(response => response.json())
        .then(data => {
            const reasonSelect = document.getElementById('reason');

            // clear exist choices
            reasonSelect.innerHTML = '';
            data.forEach(reason => {
                const option = document.createElement('option');
                option.value = reason.reasonId;  // 使用 reasonId 作为 value
                option.textContent = reason.reason;
                reasonSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error fetching reasons:', error));
}

function fetchCarriers() {
    fetch('/getAllCarrier')
        .then(response => response.json())
        .then(data => {
            const carrierSelects = document.getElementsByClassName('carrierSelect');

            for (let i = 0; i < carrierSelects.length; i++) {
                carrierSelects[i].innerHTML = '';
                data.forEach(carrier => {
                    const option = document.createElement('option');
                    option.value = carrier.carrierId;
                    option.textContent = carrier.carrierName;
                    carrierSelects[i].appendChild(option);
                });
            }
        })
        .catch(error => console.error('Error fetching carriers:', error));
}
