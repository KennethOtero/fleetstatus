// Get aircraft on startup
getAllAircraft();

setInterval(() => {
    // Update aircraft every 10 seconds
    getAllAircraft();
}, 10000);

function getAllAircraft() {
    fetch("/getAllAircraft")
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

function displayOutOfServiceAircraft(aircraftArray) {
    const table = document.getElementById("outOfServiceAircraft");

    for (let i = 0; i < aircraftArray.length; i++) {
        const card = document.createElement("div");
        card.classList.add("col-4");

        // Get correct aircraft image
        let imagePath = "";
        if (aircraftArray[i].backInService) {
            imagePath = "/images/GreenAircraft.png";
        } else {
            imagePath = "/images/redAircraft.png";
        }

        // Get reasons
        let reasonString = "";
        let length = aircraftArray[i].reason.length;
        for (let j = 0; j < length; j++) {
            if (j + 1 < length) {
                reasonString += aircraftArray[i].reason[j].reason + ", ";
            } else {
                reasonString += aircraftArray[i].reason[j].reason;
            }
        }

        // Display card
        card.innerHTML = `
        <div class="card text-white bg-dark mb-3">
            <img src="${imagePath}" alt="aircraft status image" class="card-img-top"/>
            <div class="card-body">
                <h5 class="card-title">${aircraftArray[i].tailNumber}</h5>
                <p class="card-text">${reasonString}</p>
                <p class="card-text">${aircraftArray[i].remark}</p>
                <p class="card-text">${aircraftArray[i].nextUpdate}</p>
            </div>
        </div>
        `;
        table.appendChild(card);
    }
}