package com.doritech.EmployeeService.serviceImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Specification.UserSpecification;
import com.doritech.EmployeeService.entity.EmployeeMaster;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.entity.RoleMaster;
import com.doritech.EmployeeService.entity.UserMaster;
import com.doritech.EmployeeService.exception.DuplicateResourceException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.EmployeeMasterRepository;
import com.doritech.EmployeeService.repository.ParamRepository;
import com.doritech.EmployeeService.repository.RoleMasterRepository;
import com.doritech.EmployeeService.repository.UserMasterRepository;
import com.doritech.EmployeeService.request.UserMasterRequest;
import com.doritech.EmployeeService.response.ParamResponseDTO;
import com.doritech.EmployeeService.response.UserMasterResponse;
import com.doritech.EmployeeService.service.ParamService;
import com.doritech.EmployeeService.service.UserMasterService;

import jakarta.validation.Valid;

@Service
public class UserMasterServiceImpl implements UserMasterService {

	@Autowired
	private UserMasterRepository userRepo;

	@Autowired
	private RoleMasterRepository roleRepo;

	@Autowired
	private EmployeeMasterRepository employeeRepo;

	@Autowired
	private ParamRepository paramRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ParamService paramService;

	@Override
	public ResponseEntity saveUser(@Valid UserMasterRequest request) {

		if (userRepo.existsByLoginId(request.getLoginId())) {
			throw new DuplicateResourceException("Login ID already exists");
		}

		if ("I".equalsIgnoreCase(request.getUserType())) {
			boolean employeeExists = employeeRepo.existsByEmployeeId(request.getSourceId());
			if (!employeeExists) {
				throw new ResourceNotFoundException("Employee not found with id: " + request.getSourceId());
			}
		}
		
//		if ("E".equalsIgnoreCase(request.getUserType())) {
//			boolean employeeExists = vendorRepo.existsByVendorId(request.getSourceId());
//			if (!employeeExists) {
//				throw new ResourceNotFoundException("Vendor not found with id: " + request.getSourceId());
//			}
//		}
		
		if("I".equals(request.getUserType())) {
			boolean userExists = userRepo.existsBySourceId(request.getSourceId());
			if(userExists) {
				throw new ResourceNotFoundException("User already exist with id: " + request.getSourceId());
			}
		}
		
//		if("E".equals(request.getUserType())) {
//			boolean userExists = vendorRepo.existsBySourceId(request.getSourceId());
//			if(userExists) {
//				throw new ResourceNotFoundException("User already exist with id: " + request.getSourceId());
//			}
//		}

		RoleMaster role = roleRepo.findById(request.getRoleId())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

		UserMaster user = new UserMaster();
		user.setUserType(request.getUserType());
		user.setSourceId(request.getSourceId());
		user.setLoginId(request.getLoginId());

		String combined = request.getLoginId() + request.getPassword();
		user.setPassword(passwordEncoder.encode(combined));

		user.setRole(role);
		user.setIsActive(request.getIsActive());
		user.setCreatedBy(request.getCreatedBy());

		UserMaster savedUser = userRepo.save(user);

		UserMasterResponse response = mapToResponse(savedUser);

		return new ResponseEntity("User created successfully", 201, response);
	}

	@Override
	public ResponseEntity updateUser(Integer id, @Valid UserMasterRequest request) {

	    UserMaster existingUser = userRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

	    if (!existingUser.getLoginId().equals(request.getLoginId()) &&
	            userRepo.existsByLoginId(request.getLoginId())) {
	        throw new DuplicateResourceException("Login ID already exists");
	    }

	    if ("I".equalsIgnoreCase(request.getUserType())) {
	        boolean employeeExists = employeeRepo.existsByEmployeeId(request.getSourceId());
	        if (!employeeExists) {
	            throw new ResourceNotFoundException("Employee not found with id: " + request.getSourceId());
	        }
	    }

	    // Vendor validation
//	    if ("E".equalsIgnoreCase(request.getUserType())) {
//	        boolean vendorExists = vendorRepo.existsByVendorId(request.getSourceId());
//	        if (!vendorExists) {
//	            throw new ResourceNotFoundException("Vendor not found with id: " + request.getSourceId());
//	        }
//	    }

	    if ("I".equalsIgnoreCase(request.getUserType())) {
	        boolean userExists = userRepo.existsBySourceIdAndUserIdNot(request.getSourceId(), id);
	        if (userExists) {
	            throw new DuplicateResourceException(
	                    "User already exists with sourceId: " + request.getSourceId());
	        }
	    }

//	    if ("E".equalsIgnoreCase(request.getUserType())) {
//	        boolean userExists = userRepo.existsBySourceIdAndIdNot(request.getSourceId(), id);
//	        if (userExists) {
//	            throw new DuplicateResourceException(
//	                    "User already exists with sourceId: " + request.getSourceId());
//	        }
//	    }

	    RoleMaster role = roleRepo.findById(request.getRoleId())
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Role not found with id: " + request.getRoleId()));

	    existingUser.setUserType(request.getUserType());
	    existingUser.setSourceId(request.getSourceId());
	    existingUser.setLoginId(request.getLoginId());

	    String password = request.getPassword();
	    if (password == null || password.isEmpty()) {
	        password = existingUser.getPassword();
	    }

	    if (!password.startsWith("$2a$")) {
	        password = passwordEncoder.encode(existingUser.getLoginId() + password);
	    }

	    existingUser.setPassword(password);
	    existingUser.setRole(role);
	    existingUser.setIsActive(request.getIsActive());
	    existingUser.setModifiedBy(request.getModifiedBy());

	    UserMaster updatedUser = userRepo.save(existingUser);
	    UserMasterResponse response = mapToResponse(updatedUser);

	    return new ResponseEntity("User updated successfully", 200, response);
	}

	@Override
	public ResponseEntity getUserById(Integer id) {
		UserMaster user = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		UserMasterResponse response = mapToResponse(user);

		return new ResponseEntity("User fetched successfully", 200, response);
	}

	@Override
	public ResponseEntity getAllUsers(int page, int size) {

		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException("Page must be >= 0 and size must be > 0");
		}

		Page<UserMaster> usersPage = userRepo.findAll(PageRequest.of(page, size));

		if (usersPage.isEmpty()) {
			throw new ResourceNotFoundException("No users found");
		}

		List<UserMasterResponse> responses = usersPage.getContent().stream().map(this::mapToResponse)
				.collect(Collectors.toList());

		Map<String, Object> paginationData = new HashMap<>();
		paginationData.put("content", responses);
		paginationData.put("currentPage", usersPage.getNumber());
		paginationData.put("totalItems", usersPage.getTotalElements());
		paginationData.put("totalPages", usersPage.getTotalPages());

		return new ResponseEntity("Users fetched successfully", 200, paginationData);
	}

	@Override
	public ResponseEntity deleteUserById(Integer id) {
		UserMaster existingUser = userRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		userRepo.delete(existingUser);
		return new ResponseEntity("User deleted successfully", 200, null);
	}

	@Override
	public ResponseEntity filterUsers(String loginId, String userType, Integer sourceId, Integer roleId,
			String isActive, int page, int size) {

		if (page < 0 || size <= 0) {
			throw new IllegalArgumentException("Page must be >= 0 and size must be > 0");
		}

		Page<UserMaster> usersPage = userRepo.findAll(
				UserSpecification.filter(loginId, userType, sourceId, roleId, isActive), PageRequest.of(page, size));

		List<UserMasterResponse> responses = usersPage.getContent().stream().map(this::mapToResponse).toList();

		Map<String, Object> paginationData = new HashMap<>();
		paginationData.put("content", responses);
		paginationData.put("currentPage", usersPage.getNumber());
		paginationData.put("totalItems", usersPage.getTotalElements());
		paginationData.put("totalPages", usersPage.getTotalPages());

		return new ResponseEntity("Filtered users fetched successfully", 200, paginationData);
	}

	@Override
	public ResponseEntity deleteMultipleUsers(List<Integer> userIds) {

		if (userIds == null || userIds.isEmpty()) {
			return new ResponseEntity("No user IDs provided", 400, null);
		}

		List<Integer> notFoundIds = new ArrayList<>();

		for (Integer id : userIds) {
			UserMaster user = userRepo.findById(id).orElse(null);
			if (user != null) {
				userRepo.delete(user);
			} else {
				notFoundIds.add(id);
			}
		}

		String message;
		if (notFoundIds.isEmpty()) {
			message = "All users deleted successfully";
		} else {
			message = "Users deleted. Some IDs not found: " + notFoundIds;
		}

		return new ResponseEntity(message, 200, null);
	}

	private UserMasterResponse mapToResponse(UserMaster user) {
		UserMasterResponse response = new UserMasterResponse();

		response.setUserId(user.getUserId());
		response.setUserType(user.getUserType());
		response.setPassword(user.getPassword());

		response.setUserTypeName(mapDesp2(user.getUserType(), "USER", "TYPE"));

		response.setSourceId(user.getSourceId());
		if (user.getSourceId() != null) {
			EmployeeMaster employee = employeeRepo.findById(user.getSourceId()).orElse(null);
			response.setSourceName(employee != null ? employee.getEmployeeName() : null);
		}

		response.setLoginId(user.getLoginId());

		response.setIsActive(mapDesp2(user.getIsActive(), "USER", "STATUS"));

		response.setLastLogin(user.getLastLogin());
		response.setCreatedOn(user.getCreatedOn());
		response.setModifiedOn(user.getModifiedOn());
		response.setCreatedBy(user.getCreatedBy());
		response.setModifiedBy(user.getModifiedBy());

		response.setRoleId(user.getRole() != null ? user.getRole().getRoleId() : null);

		if (user.getRole() != null) {
			response.setRoleName(user.getRole().getRoleName());
		}

		return response;
	}

	@SuppressWarnings("unchecked")
	private String mapDesp2(String value, String code, String serial) {
		if (value == null)
			return null;

		List<ParamResponseDTO> list = (List<ParamResponseDTO>) paramService.getByCodeAndSerial(code, serial);

		for (ParamResponseDTO p : list) {
			if (value.equalsIgnoreCase(p.getDesp1())) {
				return p.getDesp2();
			}
		}

		return value;
	}

	@Override
	public ResponseEntity getAllEmployeeNames() {
		List<EmployeeMaster> employees = employeeRepo.findByIsActive("Y");

		List<UserMaster> internalUsers = userRepo.findAllByUserType("I");

		List<Integer> internalUserIds = new ArrayList<>();
		for (UserMaster user : internalUsers) {
			internalUserIds.add(user.getSourceId());
		}

		List<Map<String, Object>> result = new ArrayList<>();
		for (EmployeeMaster emp : employees) {
			if (!internalUserIds.contains(emp.getEmployeeId())) {
				Map<String, Object> map = new HashMap<>();
				map.put("employeeId", emp.getEmployeeId());
				map.put("employeeName", emp.getEmployeeName());
				map.put("employeeCode", emp.getEmployeeCode());
				result.add(map);
			}
		}

		if (result.isEmpty()) {
			return new ResponseEntity("No employees found", 404, null);
		}

		return new ResponseEntity("Employees fetched successfully", 200, result);
	}
}
