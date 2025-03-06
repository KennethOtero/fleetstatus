package com.fleet.status.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fleet.status.dao.EventDAO;
import com.fleet.status.entity.Event;
import com.github.fge.jsonpatch.JsonPatch;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Profile("dev")
@RequiredArgsConstructor
public class EventService {

    private final EventDAO eventDAO;

    public void save(Event event) {
        eventDAO.save(event);
        log.info("Saved Event with Aircraft Id: {}", event.getAircraft().getAircraftId());
    }

    public Event findById(int id) {
        return eventDAO.findById(id);
    }

    @Transactional
    public List<Event> getHomepageEvents() {
        return eventDAO.getHomepageEvents();
    }

    @Transactional
    public List<Event> getOutOfServiceEvents() {
        return eventDAO.getOutOfServiceEvents();
    }

    public void showBackInService(int eventId, Instant backInServiceDate) {
        Event event = eventDAO.findById(eventId);
        if (event != null) {
            event.setEndTime(backInServiceDate);
            event.setBackInService(1);
            eventDAO.save(event);
            log.info("Set Event with Id: {} back in service", eventId);
        }
    }

    public void updateEvent(JsonPatch patch, String eventId) {
        Event eventToUpdate = eventDAO.findById(Integer.parseInt(eventId));
        if (eventToUpdate != null) {
            Event patchedEvent = patchEvent(patch, eventToUpdate);
            eventDAO.save(patchedEvent);
            log.info("Updated Event with Id: {}", eventId);
        } else {
            log.error("Event not found for ID: {}", eventId);
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Event not found for Id: " + eventId);
        }
    }

    @Transactional
    public  byte[] generateCsv(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<Event> data = getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate);

            for (Event event : data) {
                event.setCsvTailNumber(event.getAircraft().getTailNumber());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream));

            StatefulBeanToCsv<Event> sbc = new StatefulBeanToCsvBuilder<Event>(writer)
                    .withQuotechar('\'')
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            sbc.write(data);
            writer.close();

            return outputStream.toByteArray();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("Failed to generate CSV: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public byte[] generateDowntimeReport(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<Event> data = getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate);

            // 计算每个 Tail Number 的总停机时间
            Map<String, Long> tailNumberDowntimeMap = new HashMap<>();

            for (Event event : data) {
                String tail = event.getAircraft().getTailNumber();
                if (event.getStartTime() != null && event.getEndTime() != null) {
                    long downtimeMinutes = Duration.between(event.getStartTime(), event.getEndTime()).toMinutes();
                    tailNumberDowntimeMap.put(tail, tailNumberDowntimeMap.getOrDefault(tail, 0L) + downtimeMinutes);
                }
            }

            Map<String, Long> reasonDowntimeMap = new HashMap<>();

            for (Event event : data) {
                String reason = event.getReasonString();
                if (event.getStartTime() != null && event.getEndTime() != null) {
                    long downtimeMinutes = Duration.between(event.getStartTime(), event.getEndTime()).toMinutes();
                    reasonDowntimeMap.put(reason, reasonDowntimeMap.getOrDefault(reason, 0L) + downtimeMinutes);
                }
            }

            long maxDowntime = 0;
            long minDowntime = Long.MAX_VALUE;
            long totalDowntimeMinutes = 0;
            int eventCount = 0;

            for (Event event : data) {
                if (event.getStartTime() != null && event.getEndTime() != null) {
                    long downtimeMinutes = Duration.between(event.getStartTime(), event.getEndTime()).toMinutes();
                    maxDowntime = Math.max(maxDowntime, downtimeMinutes);
                    minDowntime = Math.min(minDowntime, downtimeMinutes);
                    totalDowntimeMinutes += downtimeMinutes;
                    eventCount++;
                }
            }

            long avgDowntime = eventCount > 0 ? totalDowntimeMinutes / eventCount : 0;

            Map<String, Integer> tailNumberEventCount = new HashMap<>();

            for (Event event : data) {
                String tail = event.getAircraft().getTailNumber();
                tailNumberEventCount.put(tail, tailNumberEventCount.getOrDefault(tail, 0) + 1);
            }


            // 生成 CSV 文件
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            CSVWriter csvWriter = new CSVWriter(writer);

            // 自定义 CSV 头部
            String[] header = {"Tail #", "Event Date", "End Time", "Downtime", "Reason", "Remark", "Back in Service", "Next Update"};
            csvWriter.writeNext(header);

            // 写入数据
            for (Event event : data) {
                String[] rowData = new String[]{
                        event.getAircraft().getTailNumber(),
                        event.getStartTime().toString(),
                        event.getEndTime() != null ? event.getEndTime().toString() : "N/A",
                        event.getDownTime(),
                        event.getReasonString(),
                        event.getRemark(),
                        event.getBackInService() != null && event.getBackInService() == 1 ? "Yes" : "No",
                        event.getNextUpdate() != null ? event.getNextUpdate().toString() : "N/A"
                };
                csvWriter.writeNext(rowData);
            }

            // 在 CSV 末尾添加每个 Tail Number 的总停机时间
            csvWriter.writeNext(new String[]{"", "", "", "", "", "", "", ""});
            csvWriter.writeNext(new String[]{"Tail Number", "Total Downtime"});

            calculateTimeByMinute(tailNumberDowntimeMap, csvWriter);

            csvWriter.writeNext(new String[]{"", "", "", "", "", "", "", ""});
            csvWriter.writeNext(new String[]{"Reason", "Total Downtime"});
            calculateTimeByMinute(reasonDowntimeMap, csvWriter);

            csvWriter.writeNext(new String[]{"", "", "", "", "", "", "", ""});
            csvWriter.writeNext(new String[]{"Tail Number", "Number of Events"});
            for (Map.Entry<String, Integer> entry : tailNumberEventCount.entrySet()) {
                csvWriter.writeNext(new String[]{entry.getKey(), entry.getValue().toString()});
            }

            csvWriter.writeNext(new String[]{"", "", "", "", "", "", "", ""});
            csvWriter.writeNext(new String[]{"Metric", "Value"});
            csvWriter.writeNext(new String[]{"Max Downtime", maxDowntime / (24 * 60) + "d " + (maxDowntime % (24 * 60)) / 60 + "h " + (maxDowntime % 60) + "m"});
            csvWriter.writeNext(new String[]{"Min Downtime", minDowntime / (24 * 60) + "d " + (minDowntime % (24 * 60)) / 60 + "h " + (minDowntime % 60) + "m"});
            csvWriter.writeNext(new String[]{"Average Downtime", avgDowntime / 60 + "h " + (avgDowntime % 60) + "m"});


            writer.flush();
            writer.close();
            return outputStream.toByteArray();

        } catch (IOException e) {
            log.error("Failed to generate CSV: {}", e.getMessage());
            throw new RuntimeException("Error generating CSV", e);
        }
    }

    private void calculateTimeByMinute(Map<String, Long> reasonDowntimeMap, CSVWriter csvWriter) {
        for (Map.Entry<String, Long> entry : reasonDowntimeMap.entrySet()) {
            long totalMinutes = entry.getValue();
            long days = totalMinutes / (24 * 60);
            long hours = (totalMinutes % (24 * 60)) / 60;
            long remainingMinutes = totalMinutes % 60;

            csvWriter.writeNext(new String[]{entry.getKey(), days + "d " + hours + "h " + remainingMinutes + "m"});
        }
    }


    @Transactional
    public List<Event> getFilteredEvents(Integer carrierId, Integer typeId, String tailNumber, List<Integer> reasonIds, LocalDateTime startDate, LocalDateTime endDate) {
        return eventDAO.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds, startDate, endDate);
    }

    /**
     * Converts the fields to update within the JsonPatch object to an Event object
     * @param patch - new fields to update
     * @param event - original Event
     * @return - Updated Event object
     */
    private Event patchEvent(JsonPatch patch, Event event) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Needed to handle dates
            JsonNode patched = patch.apply(mapper.convertValue(event, JsonNode.class));
            return mapper.treeToValue(patched, Event.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
