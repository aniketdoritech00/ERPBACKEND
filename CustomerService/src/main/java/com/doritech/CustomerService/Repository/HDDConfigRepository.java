package com.doritech.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.HDDConfig;

@Repository
public interface HDDConfigRepository extends JpaRepository<HDDConfig, Long> {

}
