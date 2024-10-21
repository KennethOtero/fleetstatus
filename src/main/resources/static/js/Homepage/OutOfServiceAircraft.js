// Get aircraft on startup
getAllAircraft();

// setInterval(() => {
//     // Update aircraft every 10 seconds
//     getAllAircraft();
// }, 10000);

function getAllAircraft() {
    fetch("/getAllAircraft")
        .then(response => response.json())
        .then((data) => {
            displayOutOfServiceAircraft(data);
        })
        .catch(error => console.log(error));
}

function displayOutOfServiceAircraft(aircraftArray) {
    // REMOVE ONCE DONE
    console.log(aircraftArray);

    for (let i = 0; i < aircraftArray.length; i++) {
        // Create parent div col-4
        const col_4 = document.createElement("div");
        col_4.classList.add("col-4");

        // Create card
        const card = document.createElement("div");
        card.classList.add("card", "text-white", "bg-dark", "mb-3");

        // Add fields
        const image = document.createElement("img");
        image.classList.add("card-img-top");
        image.alt = "aircraft status image";
        if (aircraftArray[i].backInService === 1) {
            image.src = "/images/GreenAircraft.png";
        } else {
            image.src = "/images/redAircraft.png";
        }

        // Create card body
        const card_body = document.createElement("div");
        card_body.classList.add("card-body");

        // Add aircraft info
        const tailNumber = document.createElement("h5");
        tailNumber.classList.add("card-title");
        tailNumber.textContent = aircraftArray[i].tailNumber;

        const reason = document.createElement("p");
        reason.classList.add("card-text");
        reason.textContent = "REASON STRING";
        for (let j = 0; j < aircraftArray[i].reason.length; i++) {
            if (j + 1 < aircraftArray[i].reason.length) {
                reason.textContent = aircraftArray[i].reason[j].reason + ", ";
            } else {
                reason.textContent = aircraftArray[i].reason[j].reason;
            }
        }

        const remark = document.createElement("p");
        remark.classList.add("card-text");
        remark.textContent = aircraftArray[i].remark;

        const nextUpdate = document.createElement("p");
        nextUpdate.classList.add("card-text");
        nextUpdate.textContent = aircraftArray[i].nextUpdate;

        // Add info to card-body
        card_body.append(tailNumber, reason, remark, nextUpdate);

        // Add card-body to card
        card.appendChild(image);
        card.appendChild(card_body);

        // Nest card div within parent col-4 div
        col_4.appendChild(card);

        // Add aircraft to DOM
        const table = document.getElementById("outOfServiceAircraft");
        table.appendChild(col_4);
    }
}