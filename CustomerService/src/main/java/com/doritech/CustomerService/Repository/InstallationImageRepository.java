package com.doritech.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.CustomerService.Entity.InstallationImage;

@Repository
public interface InstallationImageRepository extends JpaRepository<InstallationImage, Long> {


}
