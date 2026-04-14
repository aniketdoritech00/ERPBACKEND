package com.doritech.PdfService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.doritech.PdfService.Response.ResponseEntity;

@FeignClient(name = "employee-assignment-client", url = "${company.site.base-url}")
public interface EmployeeAssignmentFeignClient {

    @GetMapping("/customer/api/employeeAssignment/customer-details")
    ResponseEntity getCustomerDetails(@RequestParam("assignmentId") Integer assignmentId);

    @PutMapping("/customer/api/employeeAssignment/updateStatusAfterPdfGenerate")
    ResponseEntity updateStatusAfterPdfGenerate(@RequestParam("assignmentId") Integer assignmentId);
}