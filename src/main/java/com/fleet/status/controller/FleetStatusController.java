package com.fleet.status.controller;

import java.util.List;

import com.fleet.status.service.impl.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fleet.status.dto.Aircraft;

@Controller
@Profile("dev")
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

    @PostMapping(value="/SubmitEvent")
    public String submitEvent(@ModelAttribute("aircraftDTO")Aircraft aircraftDTO, Model model) {
        try {
            aircraftService.save(aircraftDTO);
            Aircraft blankAircraft = new Aircraft();
            model.addAttribute("aircraftDTO", blankAircraft);
            List<Aircraft> outOfServiceAircraft = aircraftService.getOutofServiceAircraft();
            model.addAttribute("outOfServiceAircraft", outOfServiceAircraft);
            return "AircraftStatus";
        } catch (Exception e) {
            throw new RuntimeException(e);
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
