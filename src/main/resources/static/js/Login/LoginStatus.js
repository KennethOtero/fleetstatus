async function checkLoginStatus() {
    try {
        const response = await fetch('/v1/auth/status');
        const data = await response.json();

        if (data.authenticated) {
            const loginStatus = document.getElementById("loginStatus");
            loginStatus.innerText = "Sign Out";
            loginStatus.href = "/logout";
        }
    } catch (error) {
        console.error('Error checking login status:', error);
    }
}

// Call the function on page load to check login status
document.addEventListener("DOMContentLoaded", async function () {
    await checkLoginStatus();
});