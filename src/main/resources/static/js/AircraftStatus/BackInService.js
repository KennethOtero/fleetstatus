// Loop through all checkboxes
$('[id^="backInService-"]').each(() => {
    // Update on click
    let aircraftId = $(this).attr('id').split('-')[1];
    $(this).addEventListener("click", (event) => {
        showBackInService(aircraftId);
    });
});

function showBackInService(aircraftID){
    $.ajax({
        url: '/showBackInService/' + aircraftID,
        type: 'PUT',
        success: function () {
            window.location.href='/AircraftStatus';
        }
    })
    ;
}