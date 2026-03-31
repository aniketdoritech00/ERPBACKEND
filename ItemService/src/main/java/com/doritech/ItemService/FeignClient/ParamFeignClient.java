package com.doritech.ItemService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.doritech.ItemService.Entity.ResponseEntity;

@FeignClient(name = "employee-service", url = "${employee.service.url}")
public interface ParamFeignClient {
	@GetMapping("/employee/api/param/getParamByCodeAndSerial")
	ResponseEntity getParamValues(@RequestParam String code,
			@RequestParam String serial);

}