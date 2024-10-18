function addAircraft() {
    removeAddAircraftErrors();

    if (validateAddAircraft()) {
        postAddAircraft();
    }
}

function removeAddAircraftErrors() {
    let inputs = getAddAircraftInputs();
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].style.removeProperty("border-color");
    }
}

function validateAddAircraft() {
    let inputs = getAddAircraftInputs();
    let result = true;

    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].value.trim().length === 0) {
            inputs[i].style.borderColor = "red";
            displayResult("addAircraftAlert", "One or more fields are empty.");
            result = false;
        }
    }

    return result;
}

function postAddAircraft() {
    let inputs = getAddAircraftInputs();

    const aircraft = {
        tailNumber:     inputs[0].value.trim(),
        carrier: {
            carrierId:  inputs[1].value.trim()
        },
        backInService: 1
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
                displayResult("addAircraftAlert", "An error occurred saving the aircraft.");
            }
        }
    });
}

function getAddAircraftInputs() {
    const tailNumber = document.getElementById("addAircraftTailNumber");
    const carrier = document.getElementById("addAircraftCarrier");

    let inputs = [];

    inputs.push(tailNumber);
    inputs.push(carrier);

    return inputs;
}