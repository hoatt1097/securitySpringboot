package com.e_rental.owner.services;

import com.e_rental.owner.entities.Owner;
import com.e_rental.owner.repositories.OwnerRepository;
import com.e_rental.owner.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<Owner> user = ownerRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Login " + usernameOrEmail + " not found");
        }
        return UserPrincipal.create(user.get());
    }

}
