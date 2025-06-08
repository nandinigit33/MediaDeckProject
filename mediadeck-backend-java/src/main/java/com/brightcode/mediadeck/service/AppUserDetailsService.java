package com.brightcode.mediadeck.service;

import com.brightcode.mediadeck.data.AppUserRepository;
import com.brightcode.mediadeck.models.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<AppUser> appUser = appUserRepository.findByUsername(username);

        if (appUser.isPresent()) {
            AppUser userObj = appUser.get();

            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(userObj.getRole())
                    .disabled(!userObj.isEnabled())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

}

