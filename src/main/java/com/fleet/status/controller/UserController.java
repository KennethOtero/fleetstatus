package com.fleet.status.controller;

import com.fleet.status.config.UriConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Profile("dev")
public class UserController {

    @GetMapping(UriConstants.URI_AUTH_STATUS)
    public ResponseEntity<Map<String, Object>> authStatus(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authentication != null);
        if (authentication != null) {
            response.put("username", authentication.getName());

            // Add roles to the response
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            List<String> roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            response.put("roles", roles);
        }
        return ResponseEntity.ok(response);
    }
}
