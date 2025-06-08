package com.brightcode.mediadeck.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PrincipalService {

    @Autowired
    AppUserRepository appUserRepository;

    // Returns currently logged-in user from SecurityContext

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public AppUser getPrincipal() {

        String username = this.getAuthentication().getName();
        Optional<AppUser> appUser =  appUserRepository.findByUsername(username);

        if (appUser.isPresent()) {
            return appUser.get();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}

