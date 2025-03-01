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
        type: {
            typeId: inputs[2].value.trim()
        }
    }

    $.ajax({
        type: "POST",
        url: "/v1/aircraft",
        data: JSON.stringify(aircraft),
        contentType: "application/json",
        statusCode: {
            201: function() {
                // Close modal and reload table
                let modalElement = document.getElementById("addAircraft");
                let modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide();
                getAircraftStatusTable();
            },
            409: function() {
                displayResult("addAircraftAlert", "This tail number already exists.");
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
    const type = document.getElementById("addAircraftType");

    let inputs = [];

    inputs.push(tailNumber);
    inputs.push(carrier);
    inputs.push(type);

    return inputs;
}

// Get updated list of carriers and types when modal is opened
$("#addAircraft").on("show.bs.modal", () => {
    fetchCarriers();
    fetchTypes();
});

function fetchCarriers() {
    fetch('/v1/carrier')
        .then(response => response.json())
        .then(data => {
            const carrierSelects = document.getElementsByClassName('carrierSelect');
            for (let i = 0; i < carrierSelects.length; i++) {
                carrierSelects[i].innerHTML = '';  // Clear the existing options
                data.forEach(carrier => {  // Change 'type' to 'carrier' here
                    const option = document.createElement("option");
                    option.value = carrier.carrierId;  // Use 'carrierId' from the correct object
                    option.textContent = carrier.carrierName;  // Use 'carrierName' from the correct object
                    carrierSelects[i].appendChild(option);
                });
            }
        })
        .catch(error => console.error('Error fetching carriers:', error));  // Corrected error message
}


function fetchTypes() {
    fetch('/v1/type')
        .then(response => response.json())
        .then(data => {
            const typeSelects = document.getElementsByClassName('typeSelect');
            for (let i = 0; i < typeSelects.length; i++) {
                typeSelects[i].innerHTML = '';
                data.forEach(type => {
                    const option = document.createElement("option")
                    option.value = type.typeId;
                    option.textContent = type.typeName;
                    typeSelects[i].appendChild(option);
                });
            }
        })
        .catch(error => console.error('Error fetching types:', error));
}