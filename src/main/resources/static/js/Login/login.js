// Simulated user database (Hardcoded users for testing)
const users = {
    "admin": { password: "admin123", role: "admin" },
    "ghana": { password: "ghana123", role: "admin" },
    "user": { password: "user123", role: "user" }
};

document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    // Handle login
    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;
            const errorMessage = document.getElementById("errorMessage");

            // Check if the username exists and password matches
            if (users[username] && users[username].password === password) {
                // Store user data in session storage
                sessionStorage.setItem("username", username);
                sessionStorage.setItem("role", users[username].role);

                // Redirect to dashboard
                window.location.href = "dashboard.html";
            } else {
                errorMessage.textContent = "Invalid username or password.";
            }
        });
    }

    // If on dashboard.html, display user info
    if (window.location.pathname.includes("dashboard.html")) {
        showContent();
    }
});

// Function to show user-specific content
function showContent() {
    const username = sessionStorage.getItem("username");
    const userRole = sessionStorage.getItem("role");

    if (!username || !userRole) {
        window.location.href = "index.html"; // Redirect if not logged in
        return;
    }

    document.getElementById("usernameDisplay").textContent = username; // Show username
    document.getElementById("userRole").textContent = userRole; // Show role

    if (userRole === "admin") {
        document.getElementById("adminSection").classList.remove("hidden");
    } else if (userRole === "user") {
        document.getElementById("userSection").classList.remove("hidden");
    }
}

// Function to log out the user
function logout() {
    sessionStorage.clear();
    window.location.href = "login.html";
}
