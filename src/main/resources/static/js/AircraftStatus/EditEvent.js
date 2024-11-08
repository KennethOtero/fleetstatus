function showEditModal(event) {
    // Open modal and load fields
    const editModal = document.getElementById("editEventModal");
    const modal = new bootstrap.Modal(editModal);
    modal.show();

    if (validateEditEvent(getEditFields())) {
        editEvent(event);
    }
}

function editEvent(event) {
    // fetch("/")
    //     .then(response => response.json())
    //     .catch(error => console.error(error));

    // Close modal
    const editModal = document.getElementById("editEventModal");
    const modal = new bootstrap.Modal(editModal);
    modal.hide();
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

    const editTailNumber = document.getElementById("editTailNumber");
    const editReason = document.getElementById("editReason");
    const editDtmNextUpdate = document.getElementById("editDtmNextUpdate");
    const editRemark = document.getElementById("editRemark");
    const editCarrier = document.getElementById("editCarrier");
    const editType = document.getElementById("editType");
    const editStartTime = document.getElementById("editTailNumber");

    fields.push(editTailNumber);
    fields.push(editReason);
    fields.push(editDtmNextUpdate);
    fields.push(editRemark);
    fields.push(editCarrier);
    fields.push(editType);
    fields.push(editStartTime);

    return fields;
}