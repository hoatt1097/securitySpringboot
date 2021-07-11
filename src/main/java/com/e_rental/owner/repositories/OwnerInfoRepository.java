package com.e_rental.owner.repositories;

import com.e_rental.owner.entities.OwnerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerInfoRepository extends JpaRepository<OwnerInfo, Long> {
}
