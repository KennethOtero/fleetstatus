package com.fleet.status.controller;

import java.util.List;

import com.fleet.status.service.impl.AircraftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fleet.status.dto.Aircraft;

@Controller
@Profile("dev")
@Slf4j
public class FleetStatusController {

    @Autowired
    private AircraftService aircraftService;

    @GetMapping({"/", "/start"})
    public String read(Model model) {
        List<Aircraft> allAircraft = aircraftService.getAllAircraft();
        model.addAttribute("allAircraft", allAircraft);
        return "start";
    }

    @GetMapping("/AircraftStatus")
    public String AircraftStatus(Model model) {
        Aircraft aircraftDTO = new Aircraft();
        model.addAttribute("aircraftDTO", aircraftDTO);
        List<Aircraft> outOfServiceAircraft = aircraftService.getOutofServiceAircraft();
        model.addAttribute("outOfServiceAircraft", outOfServiceAircraft);
        return "AircraftStatus";
    }

    @PostMapping(value="/addAircraft", produces = "application/json")
    @ResponseBody
    public String submitEvent(Aircraft aircraft) {
        try {
            aircraftService.save(aircraft);
            log.info("Tail number {} saved.", aircraft.getTailNumber());
            return "Tail number " + aircraft.getTailNumber() + " saved.";
        } catch (Exception e) {
            log.error("Failed to save tail number {}", aircraft.getTailNumber(), e);
            return "Failed to save tail number " + aircraft.getTailNumber();
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
}
