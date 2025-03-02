async function checkLoginStatus() {
    try {
        const response = await fetch(URI_AUTH_STATUS);
        const data = await response.json();
        const aircraftStatusPage = document.getElementById("aircraftStatusPage");

        if (data.authenticated) {
            const loginStatus = document.getElementById("loginStatus");
            const loginStatusText = document.getElementById("loginStatusText");
            loginStatusText.innerText = "Sign Out";
            loginStatus.href = "/logout";

            // Hide AircraftStatus page if the user isn't an Admin
            if (data.roles && data.roles.includes("ROLE_Admin")) {
                aircraftStatusPage.classList.toggle('hidden');
            }
        }
    } catch (error) {
        console.error('Error checking login status:', error);
    }
}

// Call the function on page load to check login status
document.addEventListener("DOMContentLoaded", async function () {
    await checkLoginStatus();
});