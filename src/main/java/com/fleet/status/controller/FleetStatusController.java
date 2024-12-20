package com.fleet.status.controller;

import java.util.List;

import com.fleet.status.dto.*;
import com.fleet.status.service.impl.*;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Profile("dev")
@Slf4j
public class FleetStatusController {

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private ReasonService reasonService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private EventService eventService;

    @Autowired
    private TypeService typeService;

    @GetMapping({"/", "/start"})
    public String read() {
        return "start";
    }

    @GetMapping("/AircraftStatus")
    public String AircraftStatus() {
        return "AircraftStatus";
    }

    @GetMapping("/History")
    public String History() {
        return "History";
    }

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

    @PostMapping(value = "/calcDowntime", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String calcDowntime(@RequestBody int eventId) {
        Event event = eventService.findById(eventId);

        if (event.getEndTime() == null || event.getStartTime() == null) {
            return "Down time is not available";
        }

        //long hours = eventService.calculateDownTime(event.getStartTime(), event.getEndTime());
        long hours = 0;
        return hours + " hour(s).";
    }

    @PostMapping(value = "/removeAircraft", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> removeAircraft(@RequestBody int aircraftId) {
        try {
            aircraftService.deleteAircraft(aircraftId);
            return new ResponseEntity<>("Aircraft deleted.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete aircraft with ID {}", aircraftId, e);
            return new ResponseEntity<>("Failed to delete aircraft." , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllReason")
    @ResponseBody
    public List<Reason> getReasons() {
        return reasonService.getAllReason();
    }

    @GetMapping("/getAllCarrier")
    @ResponseBody
    public List<Carrier> getAllCarriers() {
        return carrierService.getAllCarrier();
    }

    @GetMapping("/findAllAircraft")
    @ResponseBody
    public List<Aircraft> getAllAircraft() {
        return aircraftService.findAll();
    }

    @Transactional
    @RequestMapping(value="showBackInService/{eventId}", method = RequestMethod.PUT)
    public ResponseEntity<String> showBackInService (@PathVariable int eventId) {
        try {
            eventService.showBackInService(eventId);
            return new ResponseEntity<>("Aircraft back in service.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to put aircraft back in service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllTypes")
    @ResponseBody
    public List<Type> getAllTypes() {
        return typeService.findAll();
    }

    @PostMapping(value = "/saveAircraft", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> saveAircraft(@RequestBody Aircraft aircraft) {
        try {
            aircraftService.save(aircraft);
            return new ResponseEntity<>("Aircraft saved.", HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("Tail number already exists.", HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("Failed to save aircraft {}", aircraft, e);
            return new ResponseEntity<>("Failed to save aircraft.", HttpStatus.INTERNAL_SERVER_ERROR);
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
}
