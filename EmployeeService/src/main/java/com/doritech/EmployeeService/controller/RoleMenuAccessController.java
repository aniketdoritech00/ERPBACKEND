	package com.doritech.EmployeeService.controller;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.web.bind.annotation.DeleteMapping;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PathVariable;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestBody;
	import org.springframework.web.bind.annotation.RequestHeader;
	import org.springframework.web.bind.annotation.RequestMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.bind.annotation.RestController;
	
	import com.doritech.EmployeeService.entity.ResponseEntity;
	import com.doritech.EmployeeService.request.RoleMenuAccessRequest;
	import com.doritech.EmployeeService.service.RoleMenuAccessService;
	
	import jakarta.servlet.http.HttpServletRequest;
	import jakarta.validation.Valid;
	
	@RestController
	@RequestMapping("/employee/api/role-menu-access")
	public class RoleMenuAccessController {
	
		@Autowired
		private RoleMenuAccessService roleMenuAccessService;
	
		@PostMapping("/assignMenuAccess")
		public org.springframework.http.ResponseEntity<ResponseEntity> assignMenuAccess(
				@Valid @RequestBody RoleMenuAccessRequest request, @RequestHeader("X-User-Id") String userId,
				HttpServletRequest httpRequest) {
			request.setCreatedBy(Integer.parseInt(userId));
			return org.springframework.http.ResponseEntity.ok(roleMenuAccessService.assignMenuAccess(request));
		}
	
		@DeleteMapping("/unassignMenuAccess")
		public ResponseEntity unassignMenuAccess(@RequestParam("roleId") Integer roleId,
				@RequestParam("menuId") Integer menuId, @RequestHeader("X-User-Id") String userId,
				HttpServletRequest httpRequest) {
			return roleMenuAccessService.unassignMenuAccess(roleId, menuId);
		}
	
		@GetMapping("/getAllAssignMenuAccess")
		public ResponseEntity getAllRoleMenuAccess(@RequestHeader(value = "X-User-Id", required = false) String userId,
				HttpServletRequest httpRequest) {
			return roleMenuAccessService.getAllRoleMenuAccess();
		}
	
		@GetMapping("/getAssignMenuAccessByRoleId/{roleId}")
		public ResponseEntity getByRoleId(@PathVariable Integer roleId,
				@RequestHeader(value = "X-User-Id", required = false) String userId, HttpServletRequest httpRequest) {
			return roleMenuAccessService.getByRoleId(roleId);
		}
	}