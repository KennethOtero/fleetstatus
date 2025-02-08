/*
 * Contains methods to reuse across multiple pages
 */

/**
 * Catch-all to convert both T-SQL DATETIME and ISO formats to datetime-local
 * @param inputDate
 * @returns {string}
 */
function convertToDateTimeLocal(inputDate) {
    let date;
    if (inputDate.includes(' ')) {
        // Assume T-SQL DATETIME format
        date = sqlToJsDate(inputDate);
    } else {
        // Assume ISO 8601 format
        date = parseIsoDate(inputDate);
    }
    return toDateTimeLocalString(date);
}

/**
 * Turn DATETIME to JavaScript Date object
 * @param sqlDate
 * @returns {Date}
 */
function sqlToJsDate(sqlDate) {
    // sqlDate is in the format "YYYY-MM-DD HH:MM:SS"
    const [datePart, timePart] = sqlDate.split(' ');
    const [year, month, day] = datePart.split('-');
    const [hours, minutes, seconds] = timePart.split(':');
    return new Date(year, month - 1, day, hours, minutes, seconds);
}

/**
 * Parses ISO date to JavaScript Date object
 * @param isoDate
 * @returns {Date}
 */
function parseIsoDate(isoDate) {
    return new Date(isoDate);
}

/**
 * Parse the Date object to a string to use in datetime-local input
 * @param date
 * @returns {string}
 */
function toDateTimeLocalString(date) {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

/**
 * Format the nextUpdate DATETIME variable to Zulu
 * @param date
 * @returns {null|string}
 */
function formatZuluTime(date) {
    if (date !== null) {
        return date.substring(11, 16) + "z";
    }
    return null;
}

/**
 * Display error
 * @param alertBoxName
 * @param message
 */
function displayResult(alertBoxName, message) {
    const alert = document.getElementById(alertBoxName);
    alert.classList.remove("d-none");
    alert.innerText = message;

    // Hide alert after 5 seconds
    setTimeout(() => {
        if (!alert.classList.contains("d-none")) {
            alert.classList.add("d-none");
        }
    }, 5000);
}

/**
 * Convert HTML date format to SQL
 * @param datetime
 * @returns {string}
 */
function convertDateToSQL(datetime) {
    // Convert local timezone to UTC
    return new Date(datetime).toISOString();
}

/**
 * API basic auth
 * @param method
 * @returns {{headers: {Authorization: string, "Content-Type": string}, method}}
 */
function getBasicAuthHeader(method) {
    return {
        method: method,
        headers: {
            'Authorization': getBasicAuthString(),
            'Content-Type': 'application/json'
        }
    };
}

/**
 * String conversion of basic auth for endpoints
 * @returns {string}
 */
function getBasicAuthString() {
    const username = "admin";
    const password = "password";

    return `Basic ${btoa(`${username}:${password}`)}`;
}