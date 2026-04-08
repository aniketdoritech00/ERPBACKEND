package com.doritech.EmployeeService.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Specification.RoleSpecification;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.entity.RoleMaster;
import com.doritech.EmployeeService.entity.RoleMenuAccess;
import com.doritech.EmployeeService.entity.UserMaster;
import com.doritech.EmployeeService.exception.DuplicateResourceException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.ParamRepository;
import com.doritech.EmployeeService.repository.RoleMasterRepository;
import com.doritech.EmployeeService.repository.RoleMenuAccessRepository;
import com.doritech.EmployeeService.repository.UserMasterRepository;
import com.doritech.EmployeeService.request.RoleMasterRequest;
import com.doritech.EmployeeService.response.ParamResponseDTO;
import com.doritech.EmployeeService.response.RoleMasterResponse;
import com.doritech.EmployeeService.service.ParamService;
import com.doritech.EmployeeService.service.RoleMasterService;

@Service
public class RoleMasterServiceImpl implements RoleMasterService {

	@Autowired
	private RoleMasterRepository roleRepo;

	@Autowired
	UserMasterRepository userMasterRepository;

	@Autowired
	ParamRepository paramRepository;

	@Autowired
	ParamService paramService;
	@Autowired
	RoleMenuAccessRepository roleMenuAccessRepository;

	@Override
	public ResponseEntity saveRole(RoleMasterRequest request) {
		if (roleRepo.existsByRoleName(request.getRoleName())) {
			throw new DuplicateResourceException("Role name already exists");
		}

		RoleMaster role = new RoleMaster();
		role.setRoleName(request.getRoleName());
		role.setRoleDescription(request.getRoleDescription());

		role.setIsActive(request.getIsActive());
		role.setCreatedBy(request.getCreatedBy());
		// role.setModifiedBy(request.getModifiedBy());

		RoleMaster savedRole = roleRepo.save(role);

		return new ResponseEntity("Role created successfully", 201,
				mapToResponse(savedRole));
	}

	@Override
	public ResponseEntity updateRole(Integer id, RoleMasterRequest request) {
		RoleMaster existingRole = roleRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Role not found with id: " + id));

		if (!existingRole.getRoleName().equalsIgnoreCase(request.getRoleName())
				&& roleRepo.existsByRoleNameAndRoleIdNot(request.getRoleName(),
						id)) {
			throw new DuplicateResourceException("Role name already exists");
		}
		existingRole.setRoleName(request.getRoleName());
		existingRole.setRoleDescription(request.getRoleDescription());
		existingRole.setIsActive(request.getIsActive());
		existingRole.setModifiedBy(request.getModifiedBy());
		existingRole.setModifiedOn(LocalDateTime.now());

		RoleMaster updatedRole = roleRepo.save(existingRole);

		return new ResponseEntity("Role updated successfully", 200,
				mapToResponse(updatedRole));
	}

	@Override
	public ResponseEntity getRoleById(Integer id) {
		RoleMaster role = roleRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Role not found with id: " + id));
		return new ResponseEntity("Role fetched successfully", 200,
				mapToResponse(role));
	}

	@Override
	public ResponseEntity getAllRoles(int page, int size) {

		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException(
					"Page must be >= 0 and size must be > 0");
		}

		Page<RoleMaster> rolesPage = roleRepo
				.findAll(PageRequest.of(page, size));

		if (rolesPage.isEmpty()) {
			throw new ResourceNotFoundException("No roles found");
		}

		List<RoleMasterResponse> responses = rolesPage.getContent().stream()
				.map(this::mapToResponse).collect(Collectors.toList());

		Map<String, Object> paginationData = new HashMap<>();
		paginationData.put("content", responses);
		paginationData.put("currentPage", rolesPage.getNumber());
		paginationData.put("totalItems", rolesPage.getTotalElements());
		paginationData.put("totalPages", rolesPage.getTotalPages());

		return new ResponseEntity("Roles fetched successfully", 200,
				paginationData);
	}

	@Override
	public ResponseEntity deleteRole(Integer id) {

		RoleMaster existingRole = roleRepo.findById(id).orElse(null);

		if (existingRole == null) {
			return new ResponseEntity(
					"Role not found. Please check the Role ID: " + id, 404,
					null);
		}

		String roleName = existingRole.getRoleName();

		boolean isUsedInUser = userMasterRepository.existsByRole(existingRole);
		boolean isUsedInMenuAccess = roleMenuAccessRepository
				.existsByRoleMaster(existingRole);

		if (isUsedInUser && isUsedInMenuAccess) {
			return new ResponseEntity("Role " + roleName
					+ " cannot be deleted because it is used by users and also used in menu access. "
					+ "Please remove the users and menu access for this role before deleting it.",
					400, null);
		}

		if (isUsedInUser) {
			return new ResponseEntity("Role " + roleName
					+ " cannot be deleted because it is assigned to one or more users. "
					+ "Please change the users role before deleting it.", 400,
					null);
		}

		if (isUsedInMenuAccess) {
			return new ResponseEntity("Role " + roleName
					+ " cannot be deleted because it is used in menu access. "
					+ "Please remove the menu access for this role before deleting it.",
					400, null);
		}

		roleRepo.delete(existingRole);

		return new ResponseEntity("Role " + roleName + " deleted successfully.",
				200, null);
	}

	@Override
	public ResponseEntity filterRoles(String roleName, String isActive,
			Integer createdBy, Integer modifiedBy, int page, int size) {

		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException(
					"Page must be >= 0 and size must be > 0");
		}

		Page<RoleMaster> rolesPage = roleRepo.findAll(RoleSpecification
				.filter(roleName, isActive, createdBy, modifiedBy),
				PageRequest.of(page, size));

		List<RoleMasterResponse> responses = rolesPage.getContent().stream()
				.map(this::mapToResponse).toList();

		Map<String, Object> paginationData = new HashMap<>();
		paginationData.put("content", responses);
		paginationData.put("currentPage", rolesPage.getNumber());
		paginationData.put("totalItems", rolesPage.getTotalElements());
		paginationData.put("totalPages", rolesPage.getTotalPages());

		return new ResponseEntity("Filtered roles fetched successfully", 200,
				paginationData);
	}

	@Override
	public ResponseEntity deleteMultipleRoles(List<Integer> roleIds) {

		if (roleIds == null || roleIds.isEmpty()) {
			return new ResponseEntity("No roles selected for deletion.", 400,
					null);
		}

		List<RoleMaster> rolesInDB = roleRepo.findAllById(roleIds);
		Set<Integer> existingRoleIds = rolesInDB.stream()
				.map(RoleMaster::getRoleId).collect(Collectors.toSet());

		List<String> notDeletedRoles = roleIds.stream()
				.filter(id -> !existingRoleIds.contains(id))
				.map(id -> "Role ID " + id + " not found")
				.collect(Collectors.toList());

		List<UserMaster> users = userMasterRepository
				.findByRoleRoleIdIn(new ArrayList<>(existingRoleIds));
		List<RoleMenuAccess> menuAccessList = roleMenuAccessRepository
				.findByRoleMasterRoleIdIn(new ArrayList<>(existingRoleIds));

		Set<Integer> usedInUsers = users.stream()
				.map(u -> u.getRole().getRoleId()).collect(Collectors.toSet());

		Set<Integer> usedInMenu = menuAccessList.stream()
				.map(r -> r.getRoleMaster().getRoleId())
				.collect(Collectors.toSet());

		List<String> deletedRoles = new ArrayList<>();

		for (RoleMaster role : rolesInDB) {
			Integer roleId = role.getRoleId();
			String roleName = role.getRoleName();

			if (usedInUsers.contains(roleId) || usedInMenu.contains(roleId)) {
				notDeletedRoles.add(roleName);
			} else {
				roleRepo.delete(role);
				deletedRoles.add(roleName);
			}
		}

		String message;

		if (notDeletedRoles.isEmpty()) {
			message = "All selected roles deleted successfully: "
					+ deletedRoles;
		} else if (deletedRoles.isEmpty()) {
			message = "None of the selected roles could be deleted: "
					+ notDeletedRoles;
		} else {
			message = "Some roles were deleted successfully: " + deletedRoles
					+ ". Some roles could not be deleted: " + notDeletedRoles;
		}

		return new ResponseEntity(message, 200, null);
	}

	@Override
	public ResponseEntity getAllRoles() {
		List<RoleMaster> roles = roleRepo.findByIsActive("Y");

		if (roles.isEmpty()) {
			return new ResponseEntity("No active roles found", 404, null);
		}

		List<Map<String, Object>> roleList = roles.stream().map(role -> {
			Map<String, Object> map = new HashMap<>();
			map.put("roleId", role.getRoleId());
			map.put("roleName", role.getRoleName());
			return map;
		}).collect(Collectors.toList());

		return new ResponseEntity("Active roles fetched successfully", 200,
				roleList);
	}

	private RoleMasterResponse mapToResponse(RoleMaster role) {
		RoleMasterResponse response = new RoleMasterResponse();
		response.setRoleId(role.getRoleId());
		response.setRoleName(role.getRoleName());
		response.setRoleDescription(role.getRoleDescription());
		response.setIsActive(mapDesp2(role.getIsActive(), "ROLE", "STATUS"));
		response.setCreatedBy(role.getCreatedBy());
		response.setModifiedBy(role.getModifiedBy());
		response.setCreatedOn(role.getCreatedOn());
		response.setModifiedOn(role.getModifiedOn());
		return response;
	}

	@SuppressWarnings("unchecked")
	private String mapDesp2(String value, String code, String serial) {
		if (value == null)
			return null;

		List<ParamResponseDTO> list = (List<ParamResponseDTO>) paramService
				.getByCodeAndSerial(code, serial);

		for (ParamResponseDTO p : list) {
			if (value.equalsIgnoreCase(p.getDesp1())) {
				return p.getDesp2();
			}
		}

		return value;
	}
	@Override
public ResponseEntity fetchAllRoles() {
    List<RoleMaster> roles = roleRepo.findAll();

    if (roles.isEmpty()) {
        return new ResponseEntity("No roles found", 404, null);
    }

    List<Map<String, Object>> roleList = roles.stream().map(role -> {
        Map<String, Object> map = new HashMap<>();
        map.put("roleId", role.getRoleId());
        map.put("roleName", role.getRoleName());
        return map;
    }).collect(Collectors.toList());

    return new ResponseEntity("Roles fetched successfully", 200, roleList);
}

}