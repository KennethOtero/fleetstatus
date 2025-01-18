package com.fleet.status.controller;

import com.fleet.status.entity.Event;
import com.fleet.status.entity.Reason;
import com.fleet.status.service.impl.EventService;
import com.fleet.status.service.impl.ReasonService;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * All Event related endpoints
 */
@RestController
@Slf4j
@Profile("dev")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ReasonService reasonService;

    @GetMapping("/getAllAircraft")
    @ResponseBody
    public ResponseEntity<List<Event>> getHomepageAircraft() {
        try {
            return new ResponseEntity<>(eventService.getHomepageAircraft(), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOutOfServiceAircraft")
    @ResponseBody
    public ResponseEntity<List<Event>> getOutOfServiceAircraft() {
        try {
            return new ResponseEntity<>(eventService.getOutOfServiceAircraft(), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getHistory")
    @ResponseBody
    public ResponseEntity<List<Event>> getHistory(
            @RequestParam(required = false) Integer carrierId,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) String tailNumber,
            @RequestParam(required = false) List<Integer> reasonIds) {
        try {
            List<Event> events = eventService.getFilteredEvents(carrierId, typeId, tailNumber, reasonIds);
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(value="/saveEvent", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> submitEvent(@RequestBody Event event) {
        try {
            eventService.save(event);
            return new ResponseEntity<>("New event saved.", HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Tail number already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Failed to save event for tail number {}", event.getAircraft().getTailNumber(), e);
            return new ResponseEntity<>("Failed to save event.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @RequestMapping(value="showBackInService/{eventId}", method = RequestMethod.PUT)
    public ResponseEntity<String> showBackInService (@PathVariable int eventId, @RequestBody String backInServiceDate) {
        try {
            eventService.showBackInService(eventId, Instant.parse(backInServiceDate));
            return new ResponseEntity<>("Aircraft back in service.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to put aircraft back in service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findEvent/{eventId}")
    @ResponseBody
    public Event findEvent(@PathVariable int eventId) {
        return eventService.findById(eventId);
    }

    @Transactional
    @PatchMapping(value = "editEvent/{eventId}", consumes = "application/json")
    public ResponseEntity<String> editEvent(@PathVariable String eventId, @RequestBody JsonPatch patch) {
        try {
            Event eventToUpdate = eventService.findById(Integer.parseInt(eventId));
            Event patchedEvent = eventService.patchEvent(patch, eventToUpdate);
            eventService.updateEvent(patchedEvent);
            return new ResponseEntity<>("Updated event ID " + eventId + ".", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to edit event.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllReason")
    @ResponseBody
    public List<Reason> getReasons() {
        return reasonService.getAllReason();
    }
}
