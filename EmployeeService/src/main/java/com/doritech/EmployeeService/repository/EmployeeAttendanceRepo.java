package com.doritech.EmployeeService.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doritech.EmployeeService.entity.EmployeeAttendance;

@Repository
public interface EmployeeAttendanceRepo extends JpaRepository<EmployeeAttendance, Integer> {

    List<EmployeeAttendance> findByCheckInTimeBetween(
            LocalDateTime start, LocalDateTime end);

    EmployeeAttendance findByEmployee_EmployeeIdAndCheckInTimeBetweenAndCheckOutTimeIsNull(
            Integer employeeId, LocalDateTime start, LocalDateTime end);

    List<EmployeeAttendance> findByEmployee_EmployeeIdAndCheckInTimeBetween(
            Integer employeeId, LocalDateTime start, LocalDateTime end);

    List<EmployeeAttendance> findByEmployee_Site_SiteIdAndCheckInTimeBetween(
            Integer siteId, LocalDateTime start, LocalDateTime end);
}