package com.doritech.microservice.AuthenticationService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.microservice.AuthenticationService.Entity.UserMaster;

@Repository
public interface UserMasterRepository extends JpaRepository<UserMaster, Integer> {
	Optional<UserMaster> findByLoginId(String loginId);
}
