function showBackInService(aircraftID){
    $.ajax({
        url: '/showBackInService/' + aircraftID,
        type: 'PUT',
        success: function () {
            // Reload aircraft status table
            getAircraftStatusTable();
        }
    });
}