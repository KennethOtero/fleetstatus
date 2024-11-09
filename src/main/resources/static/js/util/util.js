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
        return date.substring(10, 16) + "z";
    }
    return null;
}