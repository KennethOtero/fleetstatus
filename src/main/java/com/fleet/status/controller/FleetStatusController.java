package com.fleet.status.controller;

import com.fleet.status.config.UriConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Profile("!test")
public class FleetStatusController {

    @GetMapping(UriConstants.URI_HOME)
    public String Homepage() {
        return "Homepage";
    }

    @GetMapping(UriConstants.URI_AIRCRAFT_STATUS)
    public String AircraftStatus() {
        return "AircraftStatus";
    }

    @GetMapping(UriConstants.URI_HISTORY)
    public String History() {
        return "History";
    }

    @GetMapping(UriConstants.URI_LOGIN)
    public String Login() {
        return "Login";
    }
}
