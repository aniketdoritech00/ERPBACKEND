package com.doritech.EmployeeService.service;

import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.MenuMasterRequest;

import jakarta.validation.Valid;

public interface MenuService {

	ResponseEntity getAllMenus();

	ResponseEntity getMenuById(Integer id);

	Object deleteMenu(Integer id);

	Object createMenuWithFile(@Valid MenuMasterRequest request, MultipartFile file);

	Object updateMenuIcon(Integer menuId, MultipartFile file);

	Object updateMenu(Integer menuId, @Valid MenuMasterRequest request, MultipartFile file);
}