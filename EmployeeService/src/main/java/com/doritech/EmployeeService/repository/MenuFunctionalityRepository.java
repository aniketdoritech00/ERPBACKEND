package com.doritech.EmployeeService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.MenuFunctionality;
@Repository
public interface MenuFunctionalityRepository extends JpaRepository<MenuFunctionality, Integer> {
}