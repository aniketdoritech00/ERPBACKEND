package com.doritech.CustomerService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.HDDConfig;
import com.doritech.CustomerService.Entity.Installation;

@Repository
public interface HDDConfigRepository extends JpaRepository<HDDConfig, Long> {

	Optional<HDDConfig> findByInstallation(Installation installation);

}
