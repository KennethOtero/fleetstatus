package com.fleet.status.controller;

import java.util.List;

import com.fleet.status.dto.Carrier;
import com.fleet.status.dto.Reason;
import com.fleet.status.service.impl.AircraftService;
import com.fleet.status.service.impl.CarrierService;
import com.fleet.status.service.impl.ReasonService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.fleet.status.dto.Aircraft;

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

    @GetMapping({"/", "/start"})
    public String read() {
        return "start";
    }

    @GetMapping("/AircraftStatus")
    public String AircraftStatus() {
        return "AircraftStatus";
    }

    @GetMapping("/getAllAircraft")
    @ResponseBody
    public ResponseEntity<List<Aircraft>> getHomepageAircraft() {
        try {
            return new ResponseEntity<>(aircraftService.getAllAircraft(), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOutOfServiceAircraft")
    @ResponseBody
    public ResponseEntity<List<Aircraft>> getOutOfServiceAircraft() {
        try {
            return new ResponseEntity<>(aircraftService.getOutOfServiceAircraft(), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="/addAircraftEvent", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> submitEvent(@RequestBody Aircraft aircraft) {
        try {
            aircraftService.save(aircraft);
            log.info("Tail number {} saved.", aircraft.getTailNumber());
            return new ResponseEntity<>("Aircraft saved.", HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to save aircraft with tail number {}", aircraft.getTailNumber(), e);
            return new ResponseEntity<>("Failed to save aircraft.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/calcDowntime", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String calcDowntime(@RequestBody int aircraftId) {
        Aircraft aircraft = aircraftService.fetchById(aircraftId);

        if (aircraft.getEndTime() == null || aircraft.getStartTime() == null) {
            return "Down time is not available";
        }
        long hours = aircraftService.calculateDownTime(aircraft.getStartTime(), aircraft.getEndTime());
        return hours + " hour(s).";
    }

    @PostMapping(value = "/removeAircraft", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> removeAircraft(@RequestBody int aircraftId) {
        try {
            aircraftService.deleteAircraft(aircraftId);
            log.info("Removed aircraft with ID {}", aircraftId);
            return new ResponseEntity<>("Aircraft removed.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to remove aircraft with tail number {}", aircraftId, e);
            return new ResponseEntity<>("Failed to remove aircraft." , HttpStatus.INTERNAL_SERVER_ERROR);
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
    @RequestMapping(value="showBackInService/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> showBackInService (@PathVariable int id) {
        try {
            aircraftService.showBackInService(id);
            return new ResponseEntity<>("Aircraft back in service.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to put aircraft back in service.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
