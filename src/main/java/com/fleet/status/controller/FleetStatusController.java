package com.fleet.status.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Serves HTML pages
 */
@Controller
@Profile("dev")
public class FleetStatusController {

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
}
