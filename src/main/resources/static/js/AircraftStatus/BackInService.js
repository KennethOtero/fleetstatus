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