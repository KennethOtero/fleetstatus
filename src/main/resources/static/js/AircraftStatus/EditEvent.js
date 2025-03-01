async function editEvent(eventId) {
    try {
        // Show modal
        const editModal = document.getElementById("editEventModal");
        const modal = new bootstrap.Modal(editModal);
        modal.show();

        // Get current event with ID
        const response = await fetch(URI_EVENTS + "/" + eventId);
        const event = await response.json();
        await loadEditFields(event);

        // Update event on save button click
        const submit = document.getElementById("submitEditEvent");
        const newSubmit = submit.cloneNode(true);
        submit.parentNode.replaceChild(newSubmit, submit); // Removes existing eventListeners to avoid multiple calls of sendUpdateEvent
        newSubmit.addEventListener("click", () => {
            sendUpdateEvent(event);
        });
    } catch(e) {
        console.error(e);
    }
}

async function sendUpdateEvent(event) {
    try {
        let fields = getEditFields();
        if (validateEditEvent(fields)) {
            // Get the selected reasons and turn them into an object
            const selectedReasons = Array
                .from(fields[0].selectedOptions)
                .map(option => {
                    return {
                        reasonId: option.value, // Map reasonId to Reason object
                        reason: option.textContent
                    };
                });

            const eventObject = [
                { op: "replace", path: "/reason", value: selectedReasons },
                { op: "replace", path: "/nextUpdate", value: convertDateToSQL(fields[1].value.trim()) },
                { op: "replace", path: "/remark", value: fields[2].value.trim() },
                { op: "replace", path: "/startTime", value: convertDateToSQL(fields[3].value.trim()) }
            ];

            // Send update call
            let response = await fetch(URI_EVENTS + "/" + event.eventId, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(eventObject)
            });

            if (response.status === 200) {
                // Update table after edit
                getAircraftStatusTable();

                // Close modal
                let modalElement = document.getElementById("editEventModal");
                let modal = bootstrap.Modal.getInstance(modalElement);
                modal.hide();
            } else {
                displayResult("editEventAlert", "Failed to update event for tail number " + event.aircraft.tailNumber);
            }
        } else {
            displayResult("editEventAlert", "Please enter all fields.");
        }
    } catch(e) {
        console.error(e);
    }
}

function validateEditEvent(fields) {
    let result = true;

    for (let i = 0; i < fields.length; i++) {
        if (i === 0) {
            // Check if at least one reason is selected
            if (fields[i].selectedIndex === -1) {
                fields[i].style.borderColor = "red";
                displayResult("editEventAlert", "One or more fields are empty.");
                result = false;
            }
        } else {
            if (fields[i].value.trim() === "") {
                fields[i].style.borderColor = "red";
                displayResult("editEventAlert", "One or more fields are empty.");
                result = false;
            }
        }
    }

    return result;
}

function getEditFields() {
    const fields = [];

    const editReason = document.getElementById("editReason");
    const editDtmNextUpdate = document.getElementById("editDtmNextUpdate");
    const editRemark = document.getElementById("editRemark");
    const editStartTime = document.getElementById("editStartTime");

    fields.push(editReason);
    fields.push(editDtmNextUpdate);
    fields.push(editRemark);
    fields.push(editStartTime);

    return fields;
}

/**
 * Loads the edit modal with information pertaining to the passed in Event object.
 * @param event event to be loaded in
 */
async function loadEditFields(event) {
    try {
        // Add corresponding title
        const title = document.getElementById("editEventTitle");
        title.innerText = "Edit Event for " + event.aircraft.tailNumber;

        let fields = getEditFields();

        // Get all reasons
        let response = await fetch(URI_REASON);

        let json = await response.json();

        if (json !== null) {
            // Load in reasons
            fields[0].innerHTML = '';
            json.forEach(reason => {
                const option = document.createElement("option");
                option.value = reason.reasonId;
                option.textContent = reason.reason;
                fields[0].appendChild(option);
            });

            // Select reasons matching the event
            for (const option of fields[0].options) {
                for (let i = 0; i < event.reason.length; i++) {
                    if (option.value === event.reason[i].reasonId.toString()) {
                        option.selected = true;
                    }
                }
            }

            // Populate other fields
            fields[1].value = convertToDateTimeLocal(event.nextUpdate);
            fields[2].value = event.remark;
            fields[3].value = convertToDateTimeLocal(event.startTime);
        }
    } catch(e) {
        console.error(e);
    }
}

function removeEditErrors() {
    let inputs = getEditFields();
    for (let i = 0; i < inputs.length; i++) {
        inputs[i].style.removeProperty("border-color");
    }
}