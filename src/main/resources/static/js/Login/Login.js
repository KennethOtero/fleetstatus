document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    // Store the previous URL
    sessionStorage.setItem("previousUrl", document.referrer);

    // Handle login
    if (loginForm) {
        loginForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const username = document.getElementById("username").value;
            const password = document.getElementById("password").value;

            fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
            })
                .then(response => {
                    if (response.ok) {
                        // Retrieve the previous URL from session storage
                        const previousUrl = sessionStorage.getItem("previousUrl");
                        if (previousUrl) {
                            window.location.href = previousUrl;
                        } else {
                            window.location.href = "/";
                        }
                    } else {
                        displayResult("loginAlert", "Login failed. Please check your username and password.")
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    }
});