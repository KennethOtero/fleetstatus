document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

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
                        window.location.href = "/";
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