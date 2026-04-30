package com.doritech.EmployeeService.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.EmployeeService.entity.MenuFunctionality;
import com.doritech.EmployeeService.entity.MenuMaster;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.entity.RoleMaster;
import com.doritech.EmployeeService.entity.RoleMenuAccess;
import com.doritech.EmployeeService.entity.RoleMenuFunctionality;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.MenuFunctionalityRepository;
import com.doritech.EmployeeService.repository.MenuMasterRepository;
import com.doritech.EmployeeService.repository.RoleMasterRepository;
import com.doritech.EmployeeService.repository.RoleMenuAccessRepository;
import com.doritech.EmployeeService.repository.RoleMenuFunctionalityRepository;
import com.doritech.EmployeeService.request.MenuRequest;
import com.doritech.EmployeeService.request.RoleMenuAccessRequest;
import com.doritech.EmployeeService.request.RoleMenuFunctionalityRequest;
import com.doritech.EmployeeService.response.RoleMenuAccessResponse;
import com.doritech.EmployeeService.response.RoleMenuFunctionalityResponse;
import com.doritech.EmployeeService.service.RoleMenuAccessService;

@Service
@Transactional
public class RoleMenuAccessServiceImpl implements RoleMenuAccessService {

	private static final Logger logger = LoggerFactory.getLogger(RoleMenuAccessServiceImpl.class);

	@Autowired
	private RoleMenuAccessRepository roleMenuAccessRepository;
	@Autowired
	private RoleMasterRepository roleMasterRepository;
	@Autowired
	private MenuMasterRepository menuMasterRepository;
	@Autowired
	private RoleMenuFunctionalityRepository roleMenuFunctionalityRepository;

	@Autowired
	private MenuFunctionalityRepository menuFunctionalityRepository;

	@Override
	@Transactional
	public ResponseEntity assignMenuAccess(RoleMenuAccessRequest request) {

		logger.info("Assign menu access API started");

		try {

			if (request == null) {
				throw new IllegalArgumentException("Request cannot be null");
			}

			if (request.getRoleId() == null) {
				throw new IllegalArgumentException("Role ID cannot be null");
			}

			if (request.getCreatedBy() == null) {
				throw new IllegalArgumentException("CreatedBy cannot be null");
			}

			if (request.getMenus() == null || request.getMenus().isEmpty()) {
				throw new IllegalArgumentException("Menu list cannot be empty");
			}

			RoleMaster role = roleMasterRepository.findById(request.getRoleId())
					.orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + request.getRoleId()));

			logger.info("Assigning menu access for roleId {} with {} menus", request.getRoleId(),
					request.getMenus().size());

			LocalDateTime now = LocalDateTime.now();

			for (MenuRequest menuReq : request.getMenus()) {

				if (menuReq.getMenuId() == null) {
					throw new IllegalArgumentException("Menu ID cannot be null");
				}

				MenuMaster menu = menuMasterRepository.findById(menuReq.getMenuId()).orElseThrow(
						() -> new ResourceNotFoundException("Menu not found with id: " + menuReq.getMenuId()));

				String resolvedMenuStatus = menuReq.getIsActive();

				if (resolvedMenuStatus == null || resolvedMenuStatus.trim().isEmpty()) {
					throw new IllegalArgumentException("Menu status cannot be null or empty");
				}

				if (!resolvedMenuStatus.equalsIgnoreCase("Y") && !resolvedMenuStatus.equalsIgnoreCase("N")) {
					throw new IllegalArgumentException("Menu status must be Y or N");
				}

				Optional<RoleMenuAccess> existingAccess = roleMenuAccessRepository.findByRoleMasterAndMenuMaster(role,
						menu);

				RoleMenuAccess roleMenuAccess;

				if (existingAccess.isPresent()) {

					roleMenuAccess = existingAccess.get();
					roleMenuAccess.setIsActive(resolvedMenuStatus);
					roleMenuAccess.setModifiedBy(request.getCreatedBy());
					roleMenuAccess.setModifiedOn(now);

				} else {

					roleMenuAccess = new RoleMenuAccess();
					roleMenuAccess.setRoleMaster(role);
					roleMenuAccess.setMenuMaster(menu);
					roleMenuAccess.setIsActive(resolvedMenuStatus);
					roleMenuAccess.setCreatedBy(request.getCreatedBy());
					roleMenuAccess.setCreatedOn(now);
				}

				RoleMenuAccess savedAccess = roleMenuAccessRepository.save(roleMenuAccess);

				List<RoleMenuFunctionality> existingFuncs = roleMenuFunctionalityRepository
						.findByRoleMenuAccess(savedAccess);

				Map<Integer, RoleMenuFunctionality> existingFuncMap = existingFuncs.stream()
						.collect(Collectors.toMap(f -> f.getMenuFunctionality().getMenuFuncId(), f -> f));

				List<RoleMenuFunctionality> updatedList = new ArrayList<>();

				Set<Integer> processedFuncIds = new HashSet<>();

				if (menuReq.getFunctionalities() != null && !menuReq.getFunctionalities().isEmpty()) {

					for (RoleMenuFunctionalityRequest funcReq : menuReq.getFunctionalities()) {

						if (funcReq.getMenuFuncId() == null) {
							throw new IllegalArgumentException("MenuFuncId cannot be null");
						}

						if (!processedFuncIds.add(funcReq.getMenuFuncId())) {
							throw new IllegalArgumentException(
									"Duplicate functionality id in request: " + funcReq.getMenuFuncId());
						}

						MenuFunctionality menuFunctionality = menuFunctionalityRepository
								.findById(funcReq.getMenuFuncId()).orElseThrow(() -> new ResourceNotFoundException(
										"Menu Functionality not found with id: " + funcReq.getMenuFuncId()));

						// Validate functionality belongs to menu
						if (!menuFunctionality.getMenuMaster().getMenuId().equals(menu.getMenuId())) {
							throw new IllegalArgumentException(
									"Functionality does not belong to Menu ID: " + menu.getMenuId());
						}

						String resolvedFuncStatus = funcReq.getIsActive();

						if (resolvedFuncStatus == null || resolvedFuncStatus.trim().isEmpty()) {
							throw new IllegalArgumentException("Functionality status cannot be null or empty");
						}

						if (!resolvedFuncStatus.equalsIgnoreCase("Y") && !resolvedFuncStatus.equalsIgnoreCase("N")) {
							throw new IllegalArgumentException("Functionality status must be Y or N");
						}

						RoleMenuFunctionality functionality;

						if (existingFuncMap.containsKey(funcReq.getMenuFuncId())) {

							functionality = existingFuncMap.get(funcReq.getMenuFuncId());
							functionality.setIsActive(resolvedFuncStatus);
							functionality.setModifiedBy(request.getCreatedBy());
							functionality.setModifiedOn(now);

						} else {

							functionality = new RoleMenuFunctionality();
							functionality.setRoleMenuAccess(savedAccess);
							functionality.setMenuFunctionality(menuFunctionality);
							functionality.setIsActive(resolvedFuncStatus);
							functionality.setCreatedBy(request.getCreatedBy());
							functionality.setCreatedOn(now);
						}

						updatedList.add(functionality);
					}
				}

				if (!updatedList.isEmpty()) {
					roleMenuFunctionalityRepository.saveAll(updatedList);
				}
			}

			logger.info("Role menu access assigned successfully for roleId: {}", request.getRoleId());

			return new ResponseEntity("Role menu access assigned successfully", 201, null);

		} catch (IllegalArgumentException ex) {

			logger.warn("Validation error while assigning menu access: {}", ex.getMessage());
			throw ex;

		} catch (ResourceNotFoundException ex) {

			logger.warn("Resource not found while assigning menu access: {}", ex.getMessage());
			throw ex;

		} catch (DataIntegrityViolationException ex) {

			logger.error("Database error while assigning menu access", ex);
			throw ex;

		} catch (Exception ex) {

			logger.error("Unexpected error while assigning menu access", ex);
			throw ex;
		}
	}

	@Override
	@Transactional
	public ResponseEntity unassignMenuAccess(Integer roleId, Integer menuId) {

		logger.info("Unassign role menu access request received: roleId={}, menuId={}", roleId, menuId);

		if (roleId == null) {
			throw new IllegalArgumentException("Role ID cannot be null");
		}

		if (menuId == null) {
			throw new IllegalArgumentException("Menu ID cannot be null");
		}

		RoleMenuAccess roleMenuAccess = roleMenuAccessRepository
				.findByRoleMasterRoleIdAndMenuMasterMenuId(roleId, menuId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No RoleMenuAccess found for Role ID: " + roleId + " and Menu ID: " + menuId));

		List<RoleMenuFunctionality> funcs = roleMenuFunctionalityRepository.findByRoleMenuAccess(roleMenuAccess);
		if (funcs != null && !funcs.isEmpty()) {
			roleMenuFunctionalityRepository.deleteAll(funcs);
			logger.info("Deleted {} functionalities for roleMenuAccessId={}", funcs.size(),
					roleMenuAccess.getRoleMenuId());
		}

		roleMenuAccessRepository.delete(roleMenuAccess);
		logger.info("RoleMenuAccess unassigned successfully for roleMenuAccessId={}", roleMenuAccess.getRoleMenuId());

		return new ResponseEntity("Role menu access unassigned successfully", 200, roleMenuAccess.getRoleMenuId());
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllRoleMenuAccess() {
		logger.info("getAllRoleMenuAccess API called");
		try {
			List<RoleMenuAccess> accessList = roleMenuAccessRepository.findAllWithDetails();
			logger.info("Total records fetched: {}", accessList.size());
			List<RoleMenuAccessResponse> responseList = accessList.stream().map(access -> {
				logger.debug("Processing RoleMenuAccess ID: {}", access.getRoleMenuId());
				RoleMenuAccessResponse response = new RoleMenuAccessResponse();
				response.setRoleMenuId(access.getRoleMenuId());
				if (access.getRoleMaster() != null) {
					response.setRoleId(access.getRoleMaster().getRoleId());
					response.setRoleName(access.getRoleMaster().getRoleName());
				}
				if (access.getMenuMaster() != null) {
					response.setMenuId(access.getMenuMaster().getMenuId());
					response.setMenuName(access.getMenuMaster().getMenuName());
				}
				response.setIsActive(access.getIsActive());
				List<RoleMenuFunctionalityResponse> funcList = access.getRoleMenuFunctionalities() != null
						? access.getRoleMenuFunctionalities().stream().map(func -> {
							logger.debug("Processing menuFuncId: {}", func.getMenuFunctionality().getMenuFuncId());
							RoleMenuFunctionalityResponse f = new RoleMenuFunctionalityResponse();
							f.setRoleMenuFuncId(func.getRoleMenuFuncId());
							f.setMenuFuncId(func.getMenuFunctionality().getMenuFuncId());
							f.setFunctionality(func.getMenuFunctionality().getFunctionality());
							f.setIsActive(func.getIsActive());
							return f;
						}).toList()
						: Collections.emptyList();
				response.setFunctionalities(funcList);
				return response;

			}).toList();

			logger.info("getAllRoleMenuAccess API completed successfully");
			return new ResponseEntity("Success", 200, responseList);
		} catch (Exception ex) {

			logger.error("Error occurred while fetching role menu access data", ex);
			throw new RuntimeException("Failed to fetch role menu access data");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getByRoleId(Integer roleId) {

		logger.info("getByRoleId API called with RoleId: {}", roleId);

		if (roleId == null) {
			throw new IllegalArgumentException("Role ID cannot be null");
		}

		RoleMaster role = roleMasterRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId));

		List<MenuMaster> allMenus = menuMasterRepository.findAll();

		List<RoleMenuAccess> accessList = roleMenuAccessRepository.findByRoleMaster_RoleId(roleId);

		Map<Integer, RoleMenuAccess> accessMap = accessList.stream()
				.collect(Collectors.toMap(a -> a.getMenuMaster().getMenuId(), a -> a));

		Map<Integer, RoleMenuAccessResponse> menuMap = new LinkedHashMap<>();

		for (MenuMaster menu : allMenus) {

			RoleMenuAccessResponse response = new RoleMenuAccessResponse();

			response.setMenuId(menu.getMenuId());
			response.setPath(menu.getPath());
			response.setMenuName(menu.getMenuName());
			response.setParentMenuId(menu.getParentMenuId());
			response.setSequence(menu.getSequence());
			byte[] imageBytes = menu.getIconImage();

			if (imageBytes != null) {
				String base64Image = Base64.getEncoder().encodeToString(imageBytes);
				response.setIconImage(base64Image);
			}

			response.setRoleId(role.getRoleId());
			response.setRoleName(role.getRoleName());

			RoleMenuAccess access = accessMap.get(menu.getMenuId());

			if (access != null) {

				response.setRoleMenuId(access.getRoleMenuId());
				response.setIsActive(access.getIsActive());
				response.setCreatedBy(access.getCreatedBy());
				response.setCreatedOn(access.getCreatedOn());
				response.setModifiedBy(access.getModifiedBy());
				response.setModifiedOn(access.getModifiedOn());

				List<RoleMenuFunctionalityResponse> funcList = roleMenuFunctionalityRepository
						.findByRoleMenuAccess(access).stream().map(func -> {

							RoleMenuFunctionalityResponse f = new RoleMenuFunctionalityResponse();

							f.setRoleMenuFuncId(func.getRoleMenuFuncId());
							f.setRoleMenuId(access.getRoleMenuId());
							f.setMenuFuncId(func.getMenuFunctionality().getMenuFuncId());
							f.setFunctionality(func.getMenuFunctionality().getFunctionality());
							f.setIsActive(func.getIsActive());
							f.setCreatedBy(func.getCreatedBy());
							f.setCreatedOn(func.getCreatedOn());
							f.setModifiedBy(func.getModifiedBy());
							f.setModifiedOn(func.getModifiedOn());

							return f;

						}).toList();

				response.setFunctionalities(funcList);

			} else {

				response.setIsActive("N");
				response.setFunctionalities(new ArrayList<>());
			}

			response.setChildMenus(new ArrayList<>());

			menuMap.put(menu.getMenuId(), response);
		}

		List<RoleMenuAccessResponse> parentMenus = new ArrayList<>();

		for (RoleMenuAccessResponse menu : menuMap.values()) {

			if (menu.getParentMenuId() == 0) {

				parentMenus.add(menu);

			} else {

				RoleMenuAccessResponse parent = menuMap.get(menu.getParentMenuId());

				if (parent != null) {
					parent.getChildMenus().add(menu);
				}
			}
		}

		parentMenus.sort(Comparator.comparingInt(m -> (m.getSequence() != null ? m.getSequence() : Integer.MAX_VALUE)));

		for (RoleMenuAccessResponse menu : parentMenus) {
			menu.getChildMenus().sort(
					Comparator.comparingInt(m -> (m.getSequence() != null ? m.getSequence() : Integer.MAX_VALUE)));
		}

		logger.info("Sidebar menu generated successfully for roleId {}", roleId);

		return new ResponseEntity("Menus fetched successfully", 200, parentMenus);
	}
}