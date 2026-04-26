package com.doritech.CustomerService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.Installation;

@Repository
public interface InstallationRepository extends JpaRepository<Installation, Long> {

    Optional<Installation> findByAssignmentId(Integer assignmentId);
}
