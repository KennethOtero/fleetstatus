package com.fleet.status.controller;

import com.fleet.status.entity.Aircraft;
import com.fleet.status.entity.Carrier;
import com.fleet.status.entity.Type;
import com.fleet.status.service.AircraftService;
import com.fleet.status.service.CarrierService;
import com.fleet.status.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All Aircraft related endpoints
 */
@RestController
@Slf4j
@Profile("dev")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AircraftController {

    private final AircraftService aircraftService;
    private final CarrierService carrierService;
    private final TypeService typeService;

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

    @GetMapping("/findAllAircraft")
    @ResponseBody
    public List<Aircraft> getAllAircraft() {
        return aircraftService.findAll();
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

    @GetMapping("/getAllCarrier")
    @ResponseBody
    public List<Carrier> getAllCarriers() {
        return carrierService.getAllCarrier();
    }

    @GetMapping("/getAllTypes")
    @ResponseBody
    public List<Type> getAllTypes() {
        return typeService.findAll();
    }
}
