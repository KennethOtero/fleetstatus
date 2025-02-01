
document.getElementById("submitBackInService").addEventListener("click", submitBackInService);

function showBackInService(eventId){
    // Show modal
    const eventIdHidden = document.getElementById("BISEventId");
    eventIdHidden.value = eventId;
    const dateInput = document.getElementById("enterBackInService");
    var now = new Date();
    now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
    dateInput.value = now.toISOString().slice(0,16);
    const backInServiceModal = document.getElementById("backInServiceModal");
    const modal = new bootstrap.Modal(backInServiceModal);
    modal.show();
}

function submitBackInService(){
    const eventID = document.getElementById("BISEventId").value;
    const dateInput = document.getElementById("enterBackInService");
    const dateString = dateInput.value;
    if (dateString.trim() === "") {
        dateInput.style.borderColor = "red";
        displayResult("backInServiceAlert", "Please Enter a Date/Time.");
        return;
    }
    $.ajax({
        url: '/v1/showBackInService/' + eventID,
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", getBasicAuthString())
        },
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

