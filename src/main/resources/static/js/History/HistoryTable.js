
var calendar; // **定义全局变量**

document.addEventListener("DOMContentLoaded", function() {
    // Get elements
    const startTimeInput = document.getElementById("startTime");
    const endTimeInput = document.getElementById("endTime");

    // Set the default value to today
    const now = new Date();
    const formattedDate = now.toISOString().slice(0, 16); // 'yyyy-MM-ddTHH:mm'
    startTimeInput.value = formattedDate;
    endTimeInput.value = formattedDate;
});

document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');
    calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        height: 'auto',
        contentHeight: 500,
        aspectRatio: 2,
        eventOverlap: true,  // 允许同一天多个事件
        slotEventOverlap: false,  // 让事件不会重叠

        events: function(fetchInfo, successCallback, failureCallback) {
            fetch(URI_EVENT_HISTORY)
                .then(response => response.json())
                .then(events => {
                    const formattedEvents = events.map(event => ({
                        title: `${event.tailNumber} (${event.reason})`,
                        start: event.start,
                        end: event.end || event.start,
                        color: event.color || getRandomColor(),
                        display: 'block'
                    }));
                    successCallback(formattedEvents);
                })
                .catch(error => failureCallback(error));
        }
    });
    calendar.render();
});



window.onload = function() {
    getCarriers("carrierSelect");
    getTypes("typeSelect");
    getReasons("reasonIds");
    getEventHistoryAircraft();
};

function getEventHistoryAircraft() {
    fetch(URI_AIRCRAFT)
        .then(response => response.json())
        .then(data => {
            const tailSelect = document.getElementById("tailSelect");

            // Clear old options and add default
            addDefaultSelectOption(tailSelect, "Select Tail");

            data.forEach(aircraft => {
                const option = document.createElement("option");
                option.value = aircraft.tailNumber; //Preparing for fuzzy search
                option.text = aircraft.tailNumber;
                tailSelect.appendChild(option);
            });
        })
        .catch(error => console.error("Error loading aircraft:", error));
}

function filterEventHistory(baseUrl){
    const carrierId = document.getElementById("carrierSelect").value;
    const typeId = document.getElementById("typeSelect").value;
    const tailNumber = document.getElementById("tailSelect").value;

    const startTime = document.getElementById("startTime").value;
    const endTime = document.getElementById("endTime").value;

    // Get the selected reason ID in the multi-select drop-down box
    const reasonSelect = document.getElementById("reasonIds");
    const selectedReasons = Array.from(reasonSelect.options)
        .filter(option => option.selected)
        .map(option => option.value);

    const startTimeDate = new Date(startTime);
    const endTimeDate = new Date(endTime);
    if (startTimeDate > endTimeDate) {
        alert("Start time cannot be later than end time.");
        return null;  // If time is invalid, return null to stop the request
    }

    // Constructs a URL and adds selected query parameters to the URL
    const url = new URL(baseUrl, window.location.origin);

    if (carrierId) url.searchParams.append("carrierId", carrierId);
    if (typeId) url.searchParams.append("typeId", typeId);
    if (tailNumber) url.searchParams.append("tailNumber", tailNumber);
    selectedReasons.forEach(reasonId => {
        url.searchParams.append("reasonIds", reasonId);
    });

    if (startTime) url.searchParams.append("startDate", startTime);
    if (endTime) url.searchParams.append("endDate", endTime);

    return url;
}

function getEventHistory() {
    const urlTable = filterEventHistory(URI_EVENT_HISTORY);  // 表格数据
    const urlCalendar = filterEventHistory(URI_CALENDER_EVENT_HISTORY);  // 日历数据

    // 请求表格数据
    fetch(urlTable)
        .then(response => response.json())
        .then(data => {
            const tableBody = document.getElementById("statusDisplay");
            tableBody.innerHTML = '';  // 清除旧数据

            if (data.length === 0) {
                tableBody.innerHTML = `
                <tr class="text-white text-center bg-dark">
                    <td colspan="5">No current event history.</td>
                </tr>`;
                return;
            }

            displayEventHistory(data);  // 更新表格
        })
        .catch(error => console.log("Error fetching event history:", error));

    // 请求日历数据
    fetch(urlCalendar)
        .then(response => response.json())
        .then(data => {
            updateCalendarEvents(data);  // 更新日历
        })
        .catch(error => console.log("Error fetching calendar events:", error));
}


function updateCalendarEvents(eventData) {
    // 清除现有的日历事件
    calendar.getEvents().forEach(event => event.remove());

    // 重新添加新的事件
    eventData.forEach(event => {
        calendar.addEvent({
            title: `${event.title}`,
            start: event.start,
            end: event.end || event.start,
            color: event.color,  // 颜色自动分配
            display: 'block'  // 让事件填满日历格子
        });
    });
}



function displayEventHistory(events) {
    const tableBody = document.getElementById("statusDisplay");

    events.forEach(event => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${new Date(event.startTime).toLocaleDateString()}</td>
            <td>${event.aircraft.tailNumber}</td>
            <td>${event.reasonString}</td>
            <td>${event.remark}</td>
            <td>${event.downTime}</td>
        `;
        tableBody.appendChild(row);
    });
}

function exportData() {
    const url = filterEventHistory(URI_CSV);
    const link = document.createElement('a');
    link.href = url.toString();  // The endpoint for exporting CSV
    link.click();
}

function exportDowntimeReport() {
    const url = filterEventHistory(URI_DOWNTIME_REPORT);
    const link = document.createElement('a');
    link.href = url.toString();  // The endpoint for exporting CSV
    link.click();
}