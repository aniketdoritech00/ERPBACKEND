package com.doritech.CustomerService.ValidationService;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ExternalServiceException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.FeignClient.CompSiteMappingFeignClient;
import com.doritech.CustomerService.FeignClient.CompanyClient;
import com.doritech.CustomerService.FeignClient.EmployeeFeignClient;
import com.doritech.CustomerService.FeignClient.HierarchyClient;
import com.doritech.CustomerService.FeignClient.ItemFeignClient;
import com.doritech.CustomerService.FeignClient.OrganizationClient;
import com.doritech.CustomerService.FeignClient.ParamFeignClient;
import com.doritech.CustomerService.FeignClient.SiteFeignClient;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.CompanyResponse;
import com.doritech.CustomerService.Response.CompanySiteMappingResponse;
import com.doritech.CustomerService.Response.EmployeeDTO;
import com.doritech.CustomerService.Response.HierarchyLevelResponseDTO;
import com.doritech.CustomerService.Response.HierarchyResponse;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.OrganizationResponse;
import com.doritech.CustomerService.Response.ParamResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;

@Service
public class ValidationService {

	private static final Logger logger = LoggerFactory
			.getLogger(ValidationService.class);

	@Autowired
	private SiteFeignClient siteFeignClient;

	@Autowired
	private ItemFeignClient itemFeignClient;

	@Autowired
	private OrganizationClient organizationClient;

	@Autowired
	private CompanyClient companyClient;

	@Autowired
	private HierarchyClient hierarchyClient;

	@Autowired
	private EmployeeFeignClient employeeClient;

	@Autowired
	private CompSiteMappingFeignClient compSiteFeignClient;

	@Autowired
	private ParamFeignClient paramFeignClient;

	@Autowired
	private ObjectMapper objectMapper;

	public List<EmployeeDTO> validateEmployees() {

		try {
			ResponseEntity response = employeeClient.getAllEmployees();

			if (response == null || response.getPayload() == null) {
				logger.error("Null response received from Employee Service");
				throw new ExternalServiceException(
						"Invalid response from Employee Service");
			}

			// ✅ Convert payload → List<EmployeeDTO>
			List<EmployeeDTO> empList = objectMapper.convertValue(
					response.getPayload(),
					new TypeReference<List<EmployeeDTO>>() {
					});

			if (empList == null || empList.isEmpty()) {
				logger.error("Employee list is empty");
				throw new ResourceNotFoundException("No employees found");
			}

			logger.info("Fetched {} employees successfully", empList.size());

			return empList;

		} catch (FeignException.NotFound ex) {
			logger.error("Employees not found in Employee Service: {}",
					ex.getMessage());
			throw new ResourceNotFoundException("Employees not found");

		} catch (FeignException ex) {
			logger.error("Feign error while calling Employee Service: {}",
					ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to fetch employees from Employee Service");

		} catch (Exception ex) {
			logger.error("Unexpected error while calling Employee Service: {}",
					ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Something went wrong while fetching employees");
		}
	}

	public List<CompanySiteMappingResponse> getAllCompSiteMappingByCompId(
			Integer compId) {

		try {
			ResponseEntity response = compSiteFeignClient
					.getAllCompSiteMappingByCompId(compId);

			if (response == null || response.getPayload() == null) {
				logger.error("Null response received from Employee Service");
				throw new ExternalServiceException(
						"Invalid response from Employee Service");
			}

			List<CompanySiteMappingResponse> companySiteMappingResponses = objectMapper
					.convertValue(response.getPayload(),
							new TypeReference<List<CompanySiteMappingResponse>>() {
							});

			if (companySiteMappingResponses == null
					|| companySiteMappingResponses.isEmpty()) {
				logger.error("Employee list is empty");
				throw new ResourceNotFoundException("No employees found");
			}

			logger.info("Fetched {} employees successfully",
					companySiteMappingResponses.size());

			return companySiteMappingResponses;

		} catch (FeignException.NotFound ex) {
			logger.error("Employees not found in Employee Service: {}",
					ex.getMessage());
			throw new ResourceNotFoundException("Employees not found");

		} catch (FeignException ex) {
			logger.error("Feign error while calling Employee Service: {}",
					ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to fetch employees from Employee Service");

		} catch (Exception ex) {
			logger.error("Unexpected error while calling Employee Service: {}",
					ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Something went wrong while fetching employees");
		}
	}

	public void validateSiteExists(Integer siteId) {

		if (siteId == null) {
			logger.error("Site id cannot be null");
			throw new BadRequestException("Site id cannot be null");
		}

		Boolean siteExists;

		try {
			siteExists = siteFeignClient.siteExists(siteId);

			if (siteExists == null) {
				logger.error(
						"Null response received from Site Service for siteId {}",
						siteId);
				throw new ExternalServiceException(
						"Invalid response from Site Service");
			}

		} catch (BadRequestException | ExternalServiceException ex) {
			logger.error("Error while calling Site Service for siteId {}: {}",
					siteId, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error(
					"Unexpected error while calling Site Service for siteId {}: {}",
					siteId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify site from Site Service");
		}

		if (!siteExists) {
			logger.error("Site not found with id {}", siteId);
			throw new ResourceNotFoundException(
					"Site not found with id " + siteId);
		}

		logger.info("Site verified successfully for siteId {}", siteId);
	}

	public void validateItemExists(Integer itemId) {

		if (itemId == null) {
			logger.error("Item id cannot be null");
			throw new BadRequestException("Item id cannot be null");
		}

		Boolean itemExists;

		try {
			itemExists = itemFeignClient.checkItemExists(itemId);
		} catch (Exception ex) {
			logger.error("Error while calling Item Service for itemId {}: {}",
					itemId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify item from Item Service");
		}

		if (itemExists == null || !itemExists) {
			logger.error("Item not found with id {}", itemId);
			throw new ResourceNotFoundException(
					"Item not found with id " + itemId);
		}

		logger.info("Item verified successfully for itemId {}", itemId);
	}

	public OrganizationResponse validateAndGetOrganization(Integer orgId) {

		if (orgId == null) {
			logger.error("Organization id cannot be null");
			throw new BadRequestException("Organization id cannot be null");
		}

		try {
			ResponseEntity response = organizationClient.getOrganization(orgId);

			if (response == null || response.getPayload() == null) {
				logger.error(
						"Null response from Organization Service for orgId {}",
						orgId);
				throw new ResourceNotFoundException(
						"Organization not found with id " + orgId);
			}

			Map<String, Object> payloadMap = objectMapper
					.convertValue(response.getPayload(), Map.class);

			if (payloadMap == null || payloadMap.isEmpty()) {
				logger.error("Organization not found with id {}", orgId);
				throw new ResourceNotFoundException(
						"Organization not found with id " + orgId);
			}

			OrganizationResponse org = objectMapper.convertValue(payloadMap,
					OrganizationResponse.class);

			logger.info("Organization verified successfully for orgId {}",
					orgId);

			return org;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn(
					"Validation error in validateAndGetOrganization for orgId {}: {}",
					orgId, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error(
					"Error while calling Organization Service for orgId {}: {}",
					orgId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify organization from Organization Service");
		}
	}

	public EmployeeDTO validateEmployeeExists(Integer employeeId) {

		if (employeeId == null) {
			logger.error("Employee id cannot be null");
			throw new BadRequestException("Employee id cannot be null");
		}

		try {
			ResponseEntity response = employeeClient
					.getEmployeeById(employeeId);

			if (response == null || response.getPayload() == null) {
				logger.error(
						"Null response received from Employee Service for employeeId {}",
						employeeId);
				throw new ExternalServiceException(
						"Invalid response from Employee Service");
			}

			EmployeeDTO emp = objectMapper.convertValue(response.getPayload(),
					EmployeeDTO.class);

			if (emp == null || emp.getEmployeeId() == null) {
				logger.error("Employee not found with id {}", employeeId);
				throw new ResourceNotFoundException(
						"Employee not found with id " + employeeId);
			}

			logger.info("Employee verified successfully for employeeId {}",
					employeeId);

			return emp;

		} catch (BadRequestException | ResourceNotFoundException
				| ExternalServiceException ex) {
			logger.warn(
					"Validation error in validateEmployeeExists for employeeId {}: {}",
					employeeId, ex.getMessage());
			throw ex;
		} catch (FeignException.NotFound ex) {
			logger.error("Employee not found in Employee Service for id {}: {}",
					employeeId, ex.getMessage());
			throw new ResourceNotFoundException(
					"Employee not found with id " + employeeId);
		} catch (FeignException ex) {
			logger.error(
					"Feign error while calling Employee Service for employeeId {}: {}",
					employeeId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify employee from Employee Service");
		} catch (Exception ex) {
			logger.error(
					"Unexpected error while calling Employee Service for employeeId {}: {}",
					employeeId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify employee from Employee Service");
		}
	}

	public HierarchyResponse validateAndGetHierarchy(Integer hierarchyLevelId) {

		if (hierarchyLevelId == null) {
			logger.error("Hierarchy level id cannot be null");
			throw new BadRequestException("Hierarchy level id cannot be null");
		}

		try {
			ResponseEntity response = hierarchyClient
					.getHierarchy(hierarchyLevelId);

			if (response == null || response.getPayload() == null) {
				logger.error("Null response from Hierarchy Service for id {}",
						hierarchyLevelId);
				throw new ResourceNotFoundException(
						"Hierarchy level not found with id "
								+ hierarchyLevelId);
			}

			Map<String, Object> payloadMap = objectMapper
					.convertValue(response.getPayload(), Map.class);

			Object data = payloadMap.get("data");

			if (data == null) {
				logger.error("Hierarchy level not found with id {}",
						hierarchyLevelId);
				throw new ResourceNotFoundException(
						"Hierarchy level not found with id "
								+ hierarchyLevelId);
			}

			HierarchyResponse hierarchy = objectMapper.convertValue(data,
					HierarchyResponse.class);

			logger.info("Hierarchy verified successfully for id {}",
					hierarchyLevelId);

			return hierarchy;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn(
					"Validation error in validateAndGetHierarchy for id {}: {}",
					hierarchyLevelId, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Error while calling Hierarchy Service for id {}: {}",
					hierarchyLevelId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify hierarchy from Hierarchy Service");
		}
	}

	public HierarchyLevelResponseDTO validateAndGetHierarchyLevel(
			Integer hierarchyLevelId) {

		if (hierarchyLevelId == null) {
			logger.error("Hierarchy level id cannot be null");
			throw new BadRequestException("Hierarchy level id cannot be null");
		}

		try {
			ResponseEntity response = hierarchyClient
					.getHierarchyLevel(hierarchyLevelId);

			if (response == null || response.getPayload() == null) {
				logger.error("Null response from Hierarchy Service for id {}",
						hierarchyLevelId);
				throw new ResourceNotFoundException(
						"Hierarchy level not found with id "
								+ hierarchyLevelId);
			}

			Map<String, Object> payloadMap = objectMapper
					.convertValue(response.getPayload(), Map.class);

			Object data = payloadMap.get("data");

			if (data == null) {
				logger.error("Hierarchy level not found with id {}",
						hierarchyLevelId);
				throw new ResourceNotFoundException(
						"Hierarchy level not found with id "
								+ hierarchyLevelId);
			}

			HierarchyLevelResponseDTO hierarchy = objectMapper
					.convertValue(data, HierarchyLevelResponseDTO.class);

			logger.info("Hierarchy verified successfully for id {}",
					hierarchyLevelId);

			return hierarchy;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn(
					"Validation error in validateAndGetHierarchy for id {}: {}",
					hierarchyLevelId, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Error while calling Hierarchy Service for id {}: {}",
					hierarchyLevelId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify hierarchy from Hierarchy Service");
		}
	}

	public CompanyResponse validateAndGetCompany(Integer compId) {

		if (compId == null) {
			logger.error("Company id cannot be null");
			throw new BadRequestException("Company id cannot be null");
		}

		try {
			ResponseEntity response = companyClient.getCompanyById(compId);

			if (response == null || response.getPayload() == null) {
				logger.error("Null response from Company Service for compId {}",
						compId);
				throw new ResourceNotFoundException(
						"Company not found with id " + compId);
			}

			Map<String, Object> payloadMap = objectMapper
					.convertValue(response.getPayload(), Map.class);

			Object data = payloadMap.get("data");

			if (data == null) {
				logger.error("Company not found with id {}", compId);
				throw new ResourceNotFoundException(
						"Company not found with id " + compId);
			}

			CompanyResponse company = objectMapper.convertValue(data,
					CompanyResponse.class);

			logger.info("Company verified successfully for compId {}", compId);

			return company;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn(
					"Validation error in validateAndGetCompany for compId {}: {}",
					compId, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error(
					"Error while calling Company Service for compId {}: {}",
					compId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify company from Company Service");
		}
	}

	public CompSiteResponse validateAndGetSite(Integer siteId, String type) {

		if (siteId == null) {
			logger.error("{} site id cannot be null", type);
			throw new BadRequestException(type + " site id cannot be null");
		}

		try {
			ResponseEntity response = siteFeignClient.getSiteById(siteId);

			if (response == null || response.getPayload() == null) {
				logger.error("{} site not found with id {}", type, siteId);
				throw new ResourceNotFoundException(
						type + " site not found with id " + siteId);
			}

			CompSiteResponse site = objectMapper.convertValue(
					response.getPayload(), CompSiteResponse.class);

			logger.info("{} site verified successfully for siteId {}", type,
					siteId);

			return site;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn(
					"Validation error in validateAndGetSite for {} siteId {}: {}",
					type, siteId, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error(
					"Error while calling Site Service for {} siteId {}: {}",
					type, siteId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify " + type + " site from Site Service");
		}
	}

	public ItemIDResponse validateAndGetItem(Integer itemId) {

		if (itemId == null) {
			logger.error("Item id cannot be null");
			throw new BadRequestException("Item id cannot be null");
		}

		try {
			ResponseEntity response = itemFeignClient.getItemById(itemId);

			if (response == null || response.getPayload() == null) {
				logger.error("Item not found with id {}", itemId);
				throw new ResourceNotFoundException(
						"Item not found with id " + itemId);
			}

			Map<String, Object> payloadMap = objectMapper
					.convertValue(response.getPayload(), Map.class);

			Object data = payloadMap.get("data");

			if (data == null) {
				logger.error("Item not found with id {}", itemId);
				throw new ResourceNotFoundException(
						"Item not found with id " + itemId);
			}

			ItemIDResponse item = objectMapper.convertValue(data,
					ItemIDResponse.class);

			logger.info("Item verified successfully for itemId {}", itemId);

			return item;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn(
					"Validation error in validateAndGetItem for itemId {}: {}",
					itemId, ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Error while calling Item Service for itemId {}: {}",
					itemId, ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify item from Item Service");
		}
	}

	public List<ItemIDResponse> getAllItems() {

		try {
			ResponseEntity response = itemFeignClient.getAllItems();

			if (response == null || response.getPayload() == null) {
				logger.error("Items not found ");
				throw new ResourceNotFoundException("Items not found");
			}

			Map<String, Object> payloadMap = objectMapper
					.convertValue(response.getPayload(), Map.class);

			Object data = payloadMap.get("data");

			if (data == null) {
				logger.error("Item not found ");
				throw new ResourceNotFoundException("Item not found");
			}

			// ✅ FIX HERE
			List<ItemIDResponse> itemList = objectMapper.convertValue(data,
					new TypeReference<List<ItemIDResponse>>() {
					});

			logger.info("Items fetched successfully, count: {}",
					itemList.size());

			return itemList;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn("Validation error: {}", ex.getMessage());
			throw ex;

		} catch (Exception ex) {
			logger.error("Error while calling Item Service: {}",
					ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify item from Item Service");
		}
	}

	public List<ParamResponseDTO> getParamByCodeAndSerial(String code,
			String serial) {

		try {
			ResponseEntity response = paramFeignClient
					.getParamByCodeAndSerial(code, serial);

			if (response == null || response.getPayload() == null) {
				logger.error("Items not found ");
				throw new ResourceNotFoundException("Items not found  ");
			}

			Map<String, Object> payloadMap = objectMapper
					.convertValue(response.getPayload(), Map.class);

			Object data = payloadMap.get("data");

			if (data == null) {
				logger.error("Item not found ");
				throw new ResourceNotFoundException("Item not found ");
			}

			List<ParamResponseDTO> item = objectMapper.convertValue(data,
					new TypeReference<List<ParamResponseDTO>>() {
					});

			logger.info("Item verified successfully for items {}");

			return item;

		} catch (BadRequestException | ResourceNotFoundException ex) {
			logger.warn(
					"Validation error in validateAndGetItem for itemId {}: {}",
					ex.getMessage());
			throw ex;
		} catch (Exception ex) {
			logger.error("Error while calling Item Service for itemId {}: {}",
					ex.getMessage(), ex);
			throw new ExternalServiceException(
					"Unable to verify item from Item Service");
		}
	}
	public List<CompSiteResponse> getAllSites() {
		try {
			logger.info("Calling Employee Service to fetch all sites...");

			ResponseEntity response = siteFeignClient.getAllSites();

			if (response == null) {
				logger.error(
						"Received null response from Employee Service for sites");
				throw new ResourceNotFoundException("No sites found");
			}

			if (response.getPayload() == null) {
				logger.error(
						"Received empty payload from Employee Service for sites");
				throw new ResourceNotFoundException("No sites found");
			}

			logger.info(
					"Successfully fetched response from Employee Service, converting payload...");

			List<Map<String, Object>> siteList = objectMapper
					.convertValue(response.getPayload(), List.class);

			logger.info("Number of sites fetched: {}", siteList.size());

			List<CompSiteResponse> compSiteResponses = siteList.stream()
					.map(map -> objectMapper.convertValue(map,
							CompSiteResponse.class))
					.toList();

			logger.info("Mapped {} sites into CompSiteResponse objects",
					compSiteResponses.size());

			return compSiteResponses;

		} catch (Exception ex) {
			logger.error("Error fetching sites from Employee Service", ex);
			throw new ExternalServiceException("Unable to fetch sites");
		}
	}
}