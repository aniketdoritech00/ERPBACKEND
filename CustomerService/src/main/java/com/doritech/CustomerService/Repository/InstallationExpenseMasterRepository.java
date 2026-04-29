package com.doritech.CustomerService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doritech.CustomerService.Entity.InstallationExpenseMaster;

public interface InstallationExpenseMasterRepository extends JpaRepository<InstallationExpenseMaster, Long> {
}