package com.fleet.status.controller;

import java.util.List;

import com.fleet.status.service.AircraftService;
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

    @GetMapping("/")
    public String index(Model model) {
        List<Aircraft> outOfServiceAircraft = aircraftService.getOutofServiceAircraft();
        model.addAttribute("outOfServiceAircraft", outOfServiceAircraft);
        return "start";
    }

    @GetMapping("/start")
    public String read(Model model) {
        List<Aircraft> outOfServiceAircraft = aircraftService.getOutofServiceAircraft();
        model.addAttribute("outOfServiceAircraft", outOfServiceAircraft);
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
        aircraftService.save(aircraftDTO);
        Aircraft blankAircraft = new Aircraft();
        model.addAttribute("aircraftDTO", blankAircraft);
        List<Aircraft> outOfServiceAircraft = aircraftService.getOutofServiceAircraft();
        model.addAttribute("outOfServiceAircraft", outOfServiceAircraft);
        return "AircraftStatus";
    }

}
