function updateDowntime(aircraftId) {
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
}
