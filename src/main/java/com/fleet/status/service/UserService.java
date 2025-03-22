package com.fleet.status.service;

import com.fleet.status.dao.repository.UserRepository;
import com.fleet.status.entity.User;
import com.fleet.status.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User foundUser = userRepository.findByUsername(username);

        if (foundUser == null) {
            log.error("User not found: {}", username);
            throw new UsernameNotFoundException(username);
        }

        return new UserPrincipal(foundUser);
    }
}
