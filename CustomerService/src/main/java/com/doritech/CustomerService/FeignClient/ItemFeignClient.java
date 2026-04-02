package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "item-service", url = "${item.service.url}")
public interface ItemFeignClient {

	@GetMapping("/item/api/item/exists/{itemId}")
	Boolean checkItemExists(@PathVariable Integer itemId);

	@GetMapping("/item/api/item/getItemById/{id}")
	ResponseEntity getItemById(@PathVariable("id") Integer id);

	@GetMapping("/item/api/item/getAllItems")
	ResponseEntity getAllItems();
}