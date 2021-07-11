package com.e_rental.owner.repositories;

import com.e_rental.owner.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByUsername(String username);

    boolean existsByUsername(String username);

//    Optional<Owner> findByEmail(String email);

    Optional<Owner> findByUsernameOrEmail(String username, String email);
}
