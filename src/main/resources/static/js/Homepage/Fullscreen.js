window.onload = function() {
    // Apply display: block when loading in the page to toggle from later
    const navbar = document.getElementById('sideMenu');
    navbar.style.display = 'block';
};

document.getElementById('fullscreenCheckbox').addEventListener('change', function() {
    try {
        if (this.checked) {
            if (document.documentElement.requestFullscreen) {
                toggleFullscreen();
                document.documentElement.requestFullscreen();
            } else if (document.documentElement.mozRequestFullScreen) { // Firefox
                toggleFullscreen();
                document.documentElement.mozRequestFullScreen();
            } else if (document.documentElement.webkitRequestFullscreen) { // Chrome, Safari and Opera
                toggleFullscreen();
                document.documentElement.webkitRequestFullscreen();
            } else if (document.documentElement.msRequestFullscreen) { // IE/Edge
                toggleFullscreen();
                document.documentElement.msRequestFullscreen();
            }
        } else {
            if (document.exitFullscreen) {
                toggleFullscreen();
                document.exitFullscreen();
            } else if (document.mozCancelFullScreen) { // Firefox
                toggleFullscreen();
                document.mozCancelFullScreen();
            } else if (document.webkitExitFullscreen) { // Chrome, Safari and Opera
                toggleFullscreen();
                document.webkitExitFullscreen();
            } else if (document.msExitFullscreen) { // IE/Edge
                toggleFullscreen();
                document.msExitFullscreen();
            }
        }
    } catch(e) {
        console.error(e);
    }
});

function toggleFullscreen() {
    toggleBlackBackground();
    toggleNavbar();
    toggleFullWidthContainer();
    toggleCardChange();
}

function toggleBlackBackground() {
    const body = document.body;
    if (body.classList.contains('bg-black')) {
        body.classList.remove('bg-black');
    } else {
        body.classList.add('bg-black');
    }
}

function toggleNavbar() {
    const navbar = document.getElementById('sideMenu');
    if (navbar.style.display === "block") {
        navbar.style.display = "none";
    } else if (navbar.style.display === 'none') {
        navbar.style.display = "block";
    }
}

function toggleFullWidthContainer() {
    // Hide title
    const titleContainer = document.getElementById('titleContainer');
    if (titleContainer.classList.contains('d-block')) {
        titleContainer.classList.remove('d-block');
        titleContainer.classList.add('d-none');
    } else if (titleContainer.classList.contains('d-none')) {
        titleContainer.classList.remove('d-none');
        titleContainer.classList.add('d-block');
    }

    // Make aircraft cards full-width
    const aircraftContainer = document.getElementById('aircraftContainer');
    if (aircraftContainer.classList.contains('container')) {
        aircraftContainer.classList.remove('container');
        aircraftContainer.classList.add('container-fluid');
    } else if (aircraftContainer.classList.contains('container-fluid')) {
        aircraftContainer.classList.remove('container-fluid');
        aircraftContainer.classList.add('container');
    }
}

function toggleCardChange() {
    const cards = document.querySelectorAll('.card-identifier');
    if (cards !== null) {
        cards.forEach(function(card) {
            if (card.classList.contains('col-2')) {
                // Deactivate fullscreen mode for cards
                card.classList.remove('col-2');
                card.classList.add('col-3');

                const aircraft = card.querySelectorAll('.card');
                aircraft.forEach(function(currentCard) {
                    currentCard.classList.remove('bg-black');
                    currentCard.classList.add('bg-dark');
                });
            } else if (card.classList.contains('col-3')) {
                // Activate fullscreen mode for cards
                card.classList.remove('col-3');
                card.classList.add('col-2');

                const aircraft = card.querySelectorAll('.card');
                aircraft.forEach(function(currentCard) {
                    currentCard.classList.remove('bg-dark');
                    currentCard.classList.add('bg-black');
                });
            }
        });
    }
}

function isFullscreen() {
    return !!document.fullscreenElement;
}