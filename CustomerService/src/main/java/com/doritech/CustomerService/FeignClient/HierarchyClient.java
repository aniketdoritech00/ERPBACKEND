package com.doritech.CustomerService.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.doritech.CustomerService.Entity.ResponseEntity;

@FeignClient(name = "hierarchy-service", url = "${hierarchy.service.url}")
public interface HierarchyClient {

	@GetMapping("/employee/api/hierarchy-level/getHierarchyByHierarchyLevel/{id}")
	ResponseEntity getHierarchy(@PathVariable("id") Integer id);

	@GetMapping("/employee/api/hierarchy-level/getLevelByHierarchylevelId/{hierarchylevelId}")
	ResponseEntity getHierarchyLevel(
			@PathVariable("hierarchylevelId") Integer hierarchylevelId);
}
