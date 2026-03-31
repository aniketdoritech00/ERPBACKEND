package com.doritech.EmployeeService.serviceImp;

import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.entity.MenuFunctionality;
import com.doritech.EmployeeService.entity.MenuMaster;
import com.doritech.EmployeeService.entity.ParamEntity;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.MenuMasterRepository;
import com.doritech.EmployeeService.repository.ParamRepository;
import com.doritech.EmployeeService.request.MenuMasterRequest;
import com.doritech.EmployeeService.response.MenuFunctionalityResponse;
import com.doritech.EmployeeService.response.MenuMasterResponse;
import com.doritech.EmployeeService.service.MenuService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class MenuServiceImpl implements MenuService {

	private static final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	private MenuMasterRepository menuMasterRepository;

	@Autowired
	private ParamRepository paramRepository;

	@Transactional
	@Override
	public Object createMenuWithFile(@Valid MenuMasterRequest request, MultipartFile file) {

		logger.info("Create menu request received");

		if (request == null) {
			throw new IllegalArgumentException("Request body cannot be null");
		}

		if (request.getMenuName() == null || request.getMenuName().trim().isEmpty()) {
			throw new IllegalArgumentException("Menu name cannot be empty");
		}

		try {

			MenuMaster menu = new MenuMaster();
			menu.setMenuName(request.getMenuName().trim());

			if (file != null && !file.isEmpty()) {
				menu.setIconImage(file.getBytes());
			}

			menu.setPath(request.getPath());
			menu.setSequence(request.getSequence());

			Integer parentId = request.getParentMenuId();

			if (parentId != null && parentId != 0) {

				boolean parentExists = menuMasterRepository.existsById(parentId);

				if (!parentExists) {
					logger.warn("Invalid parent menu id: {}", parentId);
					throw new ResourceNotFoundException("Parent menu not found with id: " + parentId);
				}

				menu.setParentMenuId(parentId);

			} else {
				menu.setParentMenuId(0);
			}

			String status = Optional.ofNullable(request.getIsActive()).map(String::trim).filter(s -> !s.isEmpty())
					.orElse("Active");

			menu.setIsActive("Active".equalsIgnoreCase(status) ? "Y" : "N");

			menu.setCreatedBy(request.getCreatedBy());
			menu.setModifiedBy(request.getModifiedBy());

			if (request.getFunctionalities() != null && !request.getFunctionalities().isEmpty()) {

				List<MenuFunctionality> functionalityList = request.getFunctionalities().stream().map(f -> {

					MenuFunctionality mf = new MenuFunctionality();

					String functionalityKey = f.getFunctionality().trim();

					mf.setFunctionality(paramRepository.findByDesp2IgnoreCase(functionalityKey)
							.map(ParamEntity::getDesp1).orElseThrow(
									() -> new IllegalArgumentException("Invalid Functionality: " + functionalityKey)));

					String statusKey = Optional.ofNullable(f.getIsActive()).map(String::trim).filter(s -> !s.isEmpty())
							.orElse("ACTIVE");

					mf.setIsActive("ACTIVE".equalsIgnoreCase(statusKey) ? "Y" : "N");

					mf.setCreatedBy(request.getCreatedBy());
					mf.setModifiedBy(request.getModifiedBy());
					mf.setMenuMaster(menu);

					return mf;

				}).toList();

				menu.setFunctionalities(functionalityList);
			}

			MenuMaster saved = menuMasterRepository.save(menu);

			logger.info("Menu created successfully with id {}", saved.getMenuId());

			return new ResponseEntity("Menu created successfully", 201, saved.getMenuId());

		} catch (IOException e) {

			logger.error("File upload failed", e);
			throw new RuntimeException("Failed to upload file", e);

		} catch (Exception e) {

			logger.error("Error creating menu", e);
			throw e;
		}
	}

	@Override
	public ResponseEntity getAllMenus() {

		logger.info("Fetching parent menus");

		List<MenuMaster> parentMenus = menuMasterRepository.findParentMenus();

		if (parentMenus == null || parentMenus.isEmpty()) {
			logger.warn("No menus found in database");
			throw new ResourceNotFoundException("No menus found");
		}

		List<MenuMasterResponse> responseList = parentMenus.stream()

				.filter(menu -> "Y".equalsIgnoreCase(menu.getIsActive()))

				.filter(menu -> menu.getFunctionalities() != null && menu.getFunctionalities().stream().anyMatch(
						f -> "SH".equalsIgnoreCase(f.getFunctionality()) && "Y".equalsIgnoreCase(f.getIsActive())))

				.sorted(Comparator.comparing(MenuMaster::getSequence))

				.map(parent -> {

					MenuMasterResponse parentResponse = mapToMenuResponse(parent);

					List<MenuMaster> childMenus = menuMasterRepository.findChildMenus(parent.getMenuId());

					List<MenuMasterResponse> childResponses = childMenus.stream()

							.filter(child -> "Y".equalsIgnoreCase(child.getIsActive()))

							.filter(child -> child.getFunctionalities() != null && child.getFunctionalities().stream()
									.anyMatch(f -> "SH".equalsIgnoreCase(f.getFunctionality())
											&& "Y".equalsIgnoreCase(f.getIsActive())))

							.sorted(Comparator.comparing(MenuMaster::getSequence))

							.map(this::mapToMenuResponse)

							.toList();

					parentResponse.setChildMenus(childResponses);

					return parentResponse;

				}).toList();

		logger.info("Total parent menus fetched: {}", responseList.size());

		return new ResponseEntity("Menus fetched successfully", 200, responseList);
	}

	private MenuMasterResponse mapToMenuResponse(MenuMaster menu) {

		MenuMasterResponse response = new MenuMasterResponse();

		response.setMenuId(menu.getMenuId());
		response.setMenuName(menu.getMenuName());
		response.setParentMenuId(menu.getParentMenuId());
		response.setIsActive(menu.getIsActive());
		response.setSequence(menu.getSequence());
		response.setPath(menu.getPath());

		if (menu.getIconImage() != null) {
			String base64Image = Base64.getEncoder().encodeToString(menu.getIconImage());
			response.setIconImage("data:image/jpeg;base64," + base64Image);
		}

		List<MenuFunctionalityResponse> funcList = menu.getFunctionalities().stream()

				.filter(func -> "Y".equalsIgnoreCase(func.getIsActive()))

				.map(func -> {

					MenuFunctionalityResponse funcRes = new MenuFunctionalityResponse();

					funcRes.setMenuFuncId(func.getMenuFuncId());
					funcRes.setMenuId(func.getMenuMaster().getMenuId());
					funcRes.setFunctionality(func.getFunctionality());
					funcRes.setIsActive(func.getIsActive());

					return funcRes;

				}).toList();

		response.setFunctionalities(funcList);

		return response;
	}

	@Override
	public ResponseEntity getMenuById(Integer id) {

		logger.info("Fetching menu by id {}", id);

		MenuMaster menu = menuMasterRepository.findByIdWithFunctionalities(id)
				.orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));

		return new ResponseEntity("Menu fetched successfully", 200, mapToMenuResponse(menu));
	}

	@Override
	public ResponseEntity deleteMenu(Integer id) {

		logger.info("Deleting menu {}", id);

		MenuMaster menu = menuMasterRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + id));

		menuMasterRepository.delete(menu);

		logger.info("Menu deleted {}", id);

		return new ResponseEntity("Menu deleted successfully", 200, null);
	}

	@Transactional
	@Override
	public Object updateMenuIcon(Integer menuId, MultipartFile file) {

		logger.info("Update menu icon request for menuId: {}", menuId);

		if (menuId == null) {
			throw new IllegalArgumentException("MenuId cannot be null");
		}

		try {

			MenuMaster menu = menuMasterRepository.findById(menuId)
					.orElseThrow(() -> new ResourceNotFoundException("Menu not found with id: " + menuId));

			if (file != null && !file.isEmpty()) {
				menu.setIconImage(file.getBytes());
			} else {
				throw new IllegalArgumentException("File cannot be empty");
			}

			menuMasterRepository.save(menu);

			logger.info("Menu icon updated successfully for menuId {}", menuId);

			return new ResponseEntity("Menu icon updated successfully", 200, menuId);

		} catch (IOException e) {
			logger.error("Error reading file", e);
			throw new RuntimeException("Failed to update icon", e);
		}
	}

	@Transactional
	@Override
	public Object updateMenu(Integer menuId, @Valid MenuMasterRequest request, MultipartFile file) {

		logger.info("Update menu API called for menuId: {}", menuId);

		try {

			if (menuId == null) {
				logger.error("MenuId is null");
				throw new IllegalArgumentException("MenuId cannot be null");
			}

			if (request == null) {
				logger.error("Request body is null");
				throw new IllegalArgumentException("Request body cannot be null");
			}

			MenuMaster menu = menuMasterRepository.findById(menuId).orElseThrow(() -> {
				logger.error("Menu not found with id {}", menuId);
				return new ResourceNotFoundException("Menu not found with id: " + menuId);
			});

			logger.info("Menu found with id {}", menuId);

			if (request.getMenuName() != null && !request.getMenuName().trim().isEmpty()) {
				menu.setMenuName(request.getMenuName().trim());
			}

			if (file != null && !file.isEmpty()) {
				logger.info("Updating menu icon for menuId {}", menuId);
				menu.setIconImage(file.getBytes());
			}

			menu.setPath(request.getPath());
			menu.setSequence(request.getSequence());

			Integer parentId = request.getParentMenuId();

			if (parentId != null && parentId != 0) {

				boolean parentExists = menuMasterRepository.existsById(parentId);

				if (!parentExists) {
					logger.warn("Invalid parent menu id {}", parentId);
					throw new ResourceNotFoundException("Parent menu not found with id: " + parentId);
				}

				menu.setParentMenuId(parentId);

			} else {
				menu.setParentMenuId(0);
			}

			String status = Optional.ofNullable(request.getIsActive()).map(String::trim).filter(s -> !s.isEmpty())
					.orElse("Active");

			menu.setIsActive("Active".equalsIgnoreCase(status) ? "Y" : "N");

			menu.setModifiedBy(request.getModifiedBy());

			/* -------- Functionalities Update -------- */

			if (request.getFunctionalities() != null) {

				logger.info("Updating functionalities for menuId {}", menuId);

				menu.getFunctionalities().clear();

				List<MenuFunctionality> functionalityList = request.getFunctionalities().stream().map(f -> {

					MenuFunctionality mf = new MenuFunctionality();

					String functionalityKey = f.getFunctionality().trim();

					mf.setFunctionality(paramRepository.findByDesp2IgnoreCase(functionalityKey)
							.map(ParamEntity::getDesp1).orElseThrow(
									() -> new IllegalArgumentException("Invalid Functionality: " + functionalityKey)));

					String statusKey = Optional.ofNullable(f.getIsActive()).map(String::trim).filter(s -> !s.isEmpty())
							.orElse("ACTIVE");

					mf.setIsActive("ACTIVE".equalsIgnoreCase(statusKey) ? "Y" : "N");

					mf.setCreatedBy(request.getCreatedBy());
					mf.setModifiedBy(request.getModifiedBy());
					mf.setMenuMaster(menu);

					return mf;

				}).toList();

				menu.setFunctionalities(functionalityList);
			}

			MenuMaster updatedMenu = menuMasterRepository.save(menu);

			logger.info("Menu updated successfully with id {}", updatedMenu.getMenuId());

			return new ResponseEntity("Menu updated successfully", 200, updatedMenu.getMenuId());

		}

		catch (ResourceNotFoundException ex) {
			logger.error("Resource not found error: {}", ex.getMessage());
			throw ex;
		}

		catch (IllegalArgumentException ex) {
			logger.error("Validation error: {}", ex.getMessage());
			throw ex;
		}

		catch (IOException ex) {
			logger.error("File processing failed", ex);
			throw new RuntimeException("Failed to process file");
		}

		catch (Exception ex) {
			logger.error("Unexpected error occurred while updating menu", ex);
			throw new RuntimeException("Something went wrong while updating menu");
		}
	}
}