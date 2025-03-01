// Get aircraft on startup
getAllAircraft();

setInterval(() => {
    // Update aircraft every 10 seconds
    getAllAircraft();
}, 10000);

function getAllAircraft() {
    fetch("/v1/events")
        .then(response => response.json())
        .then((data) => {
            // Remove old data
            const table = document.getElementById("outOfServiceAircraft");
            if (table.hasChildNodes()) {
                table.innerHTML = '';
            }
            displayOutOfServiceAircraft(data);
        })
        .catch(error => console.log(error));
}

function displayOutOfServiceAircraft(events) {
    const table = document.getElementById("outOfServiceAircraft");

    for (let i = 0; i < events.length; i++) {
        const card = document.createElement("div");
        card.classList.add("col-3");

        // Get correct aircraft image
        let imagePath = "";
        if (events[i].backInService) {
            imagePath = "/images/GreenAircraft.png";
        } else {
            imagePath = "/images/redAircraft.png";
        }

        // Display card
        card.innerHTML = `
        <div class="card text-white bg-dark mb-3">
            <img src="${imagePath}" alt="aircraft status image" class="card-img-top"/>
            <hr>
            <div class="card-body">
                <p class="card-title">${events[i].aircraft.tailNumber}</p>
                <hr>
                <p class="card-text">${events[i].reasonString}</p>
                <hr>
                <p class="card-text">${truncateText(events[i].remark)}</p>
                <hr>
                <p>${events[i].backInService === 0 ? "Next Update:" : "Down Time:"}</p>
                <p class="card-text">${events[i].backInService === 0 ? formatTime(events[i].nextUpdate) : events[i].downTime}</p>
            </div>
        </div>
        `;
        table.appendChild(card);
    }
}

function truncateText(text) {
    if (text.length <= 100) {
        return text;
    }
    return text.substring(0, 100) + '...';
}