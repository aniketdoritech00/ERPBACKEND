package com.doritech.PdfService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.PdfService.Response.ResponseEntity;

@FeignClient(name = "customer-service", url = "${company.site.base-url}")
public interface CustomerFeignClient {

    @GetMapping("/customer/api/getCustomerDetailsByCustomerId/{customerId}")
    ResponseEntity getCustomerDetailsByCustomerId(@PathVariable("customerId") Integer customerId);
}