package com.doritech.EmployeeService.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.MenuMasterRepository;
import com.doritech.EmployeeService.request.MenuMasterRequest;
import com.doritech.EmployeeService.service.MenuService;
import com.doritech.EmployeeService.serviceImp.MenuServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee/api/menus")
public class MenuController {

	private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuMasterRepository menuMasterRepository;

	@PostMapping(value = "createMenu", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> createMenu(@RequestPart("menu") @Valid MenuMasterRequest request,
			@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestHeader("X-User-Id") String userId, HttpServletRequest httpRequest) {
		request.setCreatedBy(Integer.parseInt(userId));
		return ResponseEntity.ok(menuService.createMenuWithFile(request, file));
	}

	@GetMapping("getAllMenus")
	public ResponseEntity<com.doritech.EmployeeService.entity.ResponseEntity> getAllMenus() {
		return ResponseEntity.ok(menuService.getAllMenus());
	}

	@GetMapping("getMenuById/{id}")
	public ResponseEntity<com.doritech.EmployeeService.entity.ResponseEntity> getMenuById(@PathVariable Integer id) {
		return ResponseEntity.ok(menuService.getMenuById(id));
	}

	@DeleteMapping("deleteMenu/{id}")
	public ResponseEntity<Object> deleteMenu(@PathVariable Integer id, @RequestHeader("X-User-Id") String userId,
			HttpServletRequest httpRequest) {
		System.out.println("Menu with ID: " + id + " deleted by user: " + userId);
		return ResponseEntity.ok(menuService.deleteMenu(id));
	}

	@PutMapping("/updateMenuIcon/{menuId}")
	public Object updateMenuIcon(@PathVariable Integer menuId, @RequestParam("file") MultipartFile file) {
		return menuService.updateMenuIcon(menuId, file);
	}

	@PutMapping(value = "/updateMenu/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> updateMenu(@PathVariable Integer menuId,
			@RequestPart("menu") @Valid MenuMasterRequest request,
			@RequestPart(value = "file", required = false) MultipartFile file) {

		return ResponseEntity.ok(menuService.updateMenu(menuId, request, file));
	}

	@GetMapping("/getMenuNames")
	public ResponseEntity<List<Map<String, Object>>> getMenuNames() {
		logger.info("Fetching all active parent menus with IDs");
		List<Map<String, Object>> menuList = menuMasterRepository.findAll().stream()
				.filter(menu -> menu.getParentMenuId() != null && menu.getParentMenuId() == 0)
				.filter(menu -> "Y".equalsIgnoreCase(menu.getIsActive())) // active only
				.map(menu -> {
					Map<String, Object> map = new HashMap<>();
					map.put("menuId", menu.getMenuId());
					map.put("menuName", menu.getMenuName());
					return map;
				}).toList();

		if (menuList.isEmpty()) {
			logger.warn("No active parent menus found");
			throw new ResourceNotFoundException("No active parent menus found");
		}
		logger.info("Total parent menus fetched: {}", menuList.size());

		return ResponseEntity.ok(menuList);
	}
}