package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "param-service", url = "${company.service.url}")
public interface ParamFeignClient {
	@GetMapping("/employee/api/param/getParamByCodeAndSerial")
	ResponseEntity getParamByCodeAndSerial(@RequestParam("code") String code,
			@RequestParam("serial") String serial);

}