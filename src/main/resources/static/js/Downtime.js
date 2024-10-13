$(document).ready(function() {
	updateDowntime();

	// update the downtime per 6s by call updateDowntime function
	setInterval(updateDowntime, 6000);
});


function updateDowntime() {

	$('[id^="downtime-"]').each(function() {
		// get aircraftId from id that start by "downtime-"
		var aircraftId = $(this).attr('id').split('-')[1];

		//literate the endpoint

		fetch('/calcDowntime', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(aircraftId)
		})
			.then(response => response.text())
			.then(data => {
				$('#downtime-' + aircraftId).html(data);
			})
			.catch(error => console.error('Error:', error));
	});
}
