function updateDowntime(eventId) {
	fetch('/calcDowntime', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(eventId)
	})
	.then(response => response.text())
	.then(data => {
		$('#downtime-' + eventId).html(data);
	})
	.catch(error => console.error('Error:', error));
}
