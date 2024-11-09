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

function loadEditFields(event) {
    let fields = getEditFields();
    fields[0].value = ""; // Get all reasons and select ones from event
    fields[1].value = ""; // Turn next update back into date picker format
    fields[2].value = event.remark;
    fields[3].value = event.startTime; // Format to date picker as well
}