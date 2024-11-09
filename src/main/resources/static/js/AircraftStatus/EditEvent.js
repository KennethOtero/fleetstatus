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

            // Update event on save
            sendUpdateEvent(data, modal);
        })
        .catch(error => console.error(error));
}

function sendUpdateEvent(event, modal) {
    let fields = getEditFields();
    if (validateEditEvent(fields)) {
        // Send update call

        // Close modal
        modal.hide();
    }
}

function validateEditEvent(fields) {
    let result = true;

    for (let i = 0; i < fields.length; i++) {
        if (fields[i].value.trim() === "") {
            result = false;
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
                    if (option.value === event.reason[i].reasonId) {
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