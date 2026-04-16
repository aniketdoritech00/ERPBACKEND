package com.doritech.EmployeeService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.PasswordPolicy;

@Repository
public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, Integer> {

    Optional<PasswordPolicy> findByStatus(String status);
}
