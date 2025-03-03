
document.getElementById("submitBackInService").addEventListener("click", submitBackInService);

function showBackInService(eventId){
    // Show modal
    const eventIdHidden = document.getElementById("BISEventId");
    const dateInput = document.getElementById("enterBackInService");

    let now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());

    eventIdHidden.value = eventId;
    dateInput.value = now.toISOString().slice(0,16);

    // Clear error highlight
    if (dateInput.style.borderColor === "red") {
        dateInput.style.removeProperty("border-color");
    }

    const backInServiceModal = document.getElementById("backInServiceModal");
    const modal = new bootstrap.Modal(backInServiceModal);

    modal.show();
}

async function submitBackInService() {
    const eventID = document.getElementById("BISEventId").value;
    const dateInput = document.getElementById("enterBackInService");
    const dateString = dateInput.value;
    if (dateString.trim() === "") {
        dateInput.style.borderColor = "red";
        displayResult("backInServiceAlert", "Please Enter a Date/Time.");
        return;
    }

    if (!await validateBackInServiceDate(eventID, dateInput)) {
        return;
    }

    $.ajax({
        url: URI_SHOW_BACK_IN_SERVICE + '/' + eventID,
        data: new Date(dateString).toISOString(),
        contentType: "application/json",
        type: 'PUT',
        success: function () {
            // Reload aircraft status table
            getAircraftStatusTable();
        }
    });
    // Close modal
    let modalElement = document.getElementById("backInServiceModal");
    let modal = bootstrap.Modal.getInstance(modalElement);
    modal.hide();
}

async function validateBackInServiceDate(eventID, dateInput) {
    if (dateInput === "") return false;

    try {
        const response = await fetch(URI_EVENTS + "/" + eventID);

        if (!response.ok) {
            displayResult("backInServiceAlert", "An error occurred saving the date.");
            return false;
        }

        const endDate = new Date(dateInput.value);
        const event = await response.json();
        const startDate = new Date(event.startTime);

        if (endDate < startDate) {
            dateInput.style.borderColor = "red";
            displayResult("backInServiceAlert", "Date cannot be before original out-of-service date.");
            return false;
        }

        return true;
    } catch (e) {
        console.error(e);
        return false;
    }
}