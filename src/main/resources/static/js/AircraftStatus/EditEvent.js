function editEvent(eventId) {
    // Show modal
    const editModal = document.getElementById("editEventModal");
    const modal = new bootstrap.Modal(editModal);
    modal.show();

    // Get current event with ID
    fetch("/findEvent/" + eventId)
        .then(response => response.json())
        .then(data => {
            // Load data into modal
            loadEditFields(data);

            // Update event on save button click
            const submit = document.getElementById("submitEditEvent");
            submit.addEventListener("click", () => {
                sendUpdateEvent(data);
            });
        })
        .catch(error => console.error(error));
}

function sendUpdateEvent(event) {
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

        const eventObject = {
            eventId: event.eventId,
            aircraftId: event.aircraftId,
            reason: selectedReasons,
            nextUpdate: convertDateToSQL(fields[1].value.trim()),
            remark: fields[2].value.trim(),
            startTime: convertDateToSQL(fields[3].value.trim()),
        }

        // Send update call
        fetch("/editEvent", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(eventObject)
        })
        .then(response => response.json())
        .catch(error => console.error(error));

        // Close modal
        let modalElement = document.getElementById("editEventModal");
        let modal = bootstrap.Modal.getInstance(modalElement);
        modal.hide();
    } else {
        displayResult("editEventAlert", "Please enter all fields.");
    }
}

function validateEditEvent(fields) {
    let result = true;

    for (let i = 0; i < fields.length; i++) {
        if (i === 0) {
            // Check if at least one reason is selected
            if (fields[i].selectedIndex === -1) {
                result = false;
            }
        } else {
            if (fields[i].value.trim() === "") {
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
function loadEditFields(event) {
    // Add corresponding title
    const title = document.getElementById("editEventTitle");
    title.innerText = "Edit Event for " + event.aircraft.tailNumber;

    let fields = getEditFields();

    // Get all reasons
    fetch("/getAllReason")
        .then(response => response.json())
        .then(data => {
            // Add options to editReason multi-select box
            fields[0].innerHTML = '';
            data.forEach(reason => {
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
        })
        .catch(error => console.error(error));
}