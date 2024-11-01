function showBackInService(eventId){
    $.ajax({
        url: '/showBackInService/' + eventId,
        type: 'PUT',
        success: function () {
            // Reload aircraft status table
            getAircraftStatusTable();
        }
    });
}