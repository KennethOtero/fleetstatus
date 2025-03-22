package com.fleet.status.controller;

import com.fleet.status.config.UriConstants;
import com.fleet.status.entity.Aircraft;
import com.fleet.status.entity.Carrier;
import com.fleet.status.entity.Type;
import com.fleet.status.service.AircraftService;
import com.fleet.status.service.CarrierService;
import com.fleet.status.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AircraftController {

    private final AircraftService aircraftService;
    private final CarrierService carrierService;
    private final TypeService typeService;

    @DeleteMapping(value = UriConstants.URI_AIRCRAFT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> removeAircraft(@RequestParam int aircraftId) {
        try {
            aircraftService.deleteAircraft(aircraftId);
            return new ResponseEntity<>("Aircraft deleted.", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to delete aircraft with ID {}", aircraftId, e);
            return new ResponseEntity<>("Failed to delete aircraft." , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(UriConstants.URI_AIRCRAFT)
    @ResponseBody
    public List<Aircraft> getAllAircraft() {
        return aircraftService.findAll();
    }

    @PostMapping(value = UriConstants.URI_AIRCRAFT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    @GetMapping(UriConstants.URI_CARRIER)
    @ResponseBody
    public List<Carrier> getAllCarriers() {
        return carrierService.getAllCarrier();
    }

    @GetMapping(UriConstants.URI_TYPE)
    @ResponseBody
    public List<Type> getAllTypes() {
        return typeService.findAll();
    }
}
