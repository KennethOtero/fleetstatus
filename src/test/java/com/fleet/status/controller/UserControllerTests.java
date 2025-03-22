package com.fleet.status.controller;

import com.fleet.status.config.UriConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class UserControllerTests {

    private MockMvc mockMvc;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = mock(Authentication.class);
        UserController userController = new UserController();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testAuthStatusAuthenticated() throws Exception {
        when(authentication.getName()).thenReturn("testUser");
        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(authentication.isAuthenticated()).thenReturn(true);

        // Set the authentication in the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get(UriConstants.URI_AUTH_STATUS)
                        .principal(authentication)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.authenticated").value(true))
                        .andExpect(jsonPath("$.username").value("testUser"))
                        .andExpect(jsonPath("$.roles").isArray())
                        .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"))
                        .andExpect(jsonPath("$.roles[1]").value("ROLE_USER"));
    }

    @Test
    void testAuthStatusNotAuthenticated() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(null);
        when(authentication.getAuthorities()).thenReturn(List.of());
        when(authentication.isAuthenticated()).thenReturn(false);

        // Set the authentication in the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(get(UriConstants.URI_AUTH_STATUS)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.authenticated").value(false))
                        .andExpect(jsonPath("$.username").doesNotExist())
                        .andExpect(jsonPath("$.roles").doesNotExist());
    }
}
