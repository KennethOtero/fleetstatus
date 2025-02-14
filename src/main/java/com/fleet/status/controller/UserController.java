package com.fleet.status.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * All User-related endpoints
 */
@RestController
@Slf4j
@Profile("dev")
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserController {

    @GetMapping("/auth/status")
    public ResponseEntity<Map<String, Object>> authStatus(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authentication != null);
        if (authentication != null) {
            response.put("username", authentication.getName());
        }
        return ResponseEntity.ok(response);
    }
}
