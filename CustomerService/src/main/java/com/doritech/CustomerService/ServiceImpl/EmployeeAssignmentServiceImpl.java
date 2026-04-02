package com.doritech.CustomerService.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doritech.CustomerService.Entity.ContractEntityMapping;
import com.doritech.CustomerService.Entity.ContractItemMapping;
import com.doritech.CustomerService.Entity.ContractMaster;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.EmployeeAssignmentEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.ContractEntityMappingRepository;
import com.doritech.CustomerService.Repository.ContractItemMappingRepository;
import com.doritech.CustomerService.Repository.ContractMasterRepository;
import com.doritech.CustomerService.Repository.EmployeeAssignmentRepository;
import com.doritech.CustomerService.Request.EmployeeAssignmentRequest;
import com.doritech.CustomerService.Response.CompSiteResponse;
import com.doritech.CustomerService.Response.CompanySiteMappingResponse;
import com.doritech.CustomerService.Response.CustomerResponse;
import com.doritech.CustomerService.Response.EmployeeAssignmentResponse;
import com.doritech.CustomerService.Response.EmployeeDTO;
import com.doritech.CustomerService.Response.HierarchyLevelResponseDTO;
import com.doritech.CustomerService.Response.ItemIDResponse;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Response.ParamResponseDTO;
import com.doritech.CustomerService.Service.EmployeeAssignmentService;
import com.doritech.CustomerService.ValidationService.ValidationService;

import jakarta.transaction.Transactional;

@Service
public class EmployeeAssignmentServiceImpl
		implements
			EmployeeAssignmentService {

	@Autowired
	private EmployeeAssignmentRepository repository;

	@Autowired
	private ContractMasterRepository contractMasterRepository;

	@Autowired
	private ContractItemMappingRepository contractItemMappingRepository;
	@Autowired
	private ContractEntityMappingRepository contractEntityMappingRepository;

	@Autowired
	private ValidationService validationService;

	@Override
	@Transactional
	public EmployeeAssignmentResponse saveEmployeeAssignment(
			EmployeeAssignmentRequest request) {

		EmployeeAssignmentEntity entity = new EmployeeAssignmentEntity();

		Optional<ContractMaster> contractMaster = contractMasterRepository
				.findById(request.getContractId());

		if (contractMaster.isEmpty()) {
			throw new ResourceNotFoundException(
					"Contract not Found with this ID "
							+ request.getContractId());
		}

		entity.setContract(contractMaster.get());
		entity.setEmployeeId(request.getEmployeeId());
		entity.setSiteId(request.getSiteId());
		entity.setAssignmentStartDate(request.getAssignmentStartDate());
		entity.setAssignmentEndDate(request.getAssignmentEndDate());
		entity.setStatus("Pending");
		entity.setRemark("NA");
		entity.setCreatedBy(request.getCreatedBy());

		EmployeeAssignmentEntity saved = repository.save(entity);

		return mapToResponse(saved);
	}

	@Override
	@Transactional
	public List<EmployeeAssignmentResponse> saveBulkEmployeeAssignment(
			List<EmployeeAssignmentRequest> requests) {

		if (requests == null || requests.isEmpty()) {
			throw new ResourceNotFoundException("Request list cannot be empty");
		}

		Set<Integer> contractIds = requests.stream()
				.map(EmployeeAssignmentRequest::getContractId)
				.collect(Collectors.toSet());

		Map<Integer, ContractMaster> contractMap = contractMasterRepository
				.findAllById(contractIds).stream().collect(Collectors
						.toMap(ContractMaster::getContractId, c -> c));

		List<EmployeeAssignmentEntity> entities = new ArrayList<>();

		for (EmployeeAssignmentRequest request : requests) {

			ContractMaster contract = contractMap.get(request.getContractId());

			if (contract == null) {
				throw new ResourceNotFoundException(
						"Contract not found with ID "
								+ request.getContractId());
			}

			EmployeeAssignmentEntity entity = new EmployeeAssignmentEntity();

			entity.setContract(contract);
			entity.setEmployeeId(request.getEmployeeId());
			entity.setSiteId(request.getSiteId());
			entity.setAssignmentStartDate(request.getAssignmentStartDate());
			entity.setAssignmentEndDate(request.getAssignmentEndDate());
			entity.setVisitDate(request.getVisitDate());
			entity.setStatus("Pending");
			entity.setRemark("NA");
			entity.setCreatedBy(request.getCreatedBy());

			entities.add(entity);
		}

		List<EmployeeAssignmentEntity> savedEntities = repository
				.saveAll(entities);

		return savedEntities.stream().map(this::mapToResponse).toList();
	}

	@Override
	public PageResponse<EmployeeAssignmentResponse> getEmployeeAssignments(
			Integer employeeId, int page, int size, String sortBy,
			String sortDir) {

		// 🔥 Common Validation
		if (page < 0) {
			throw new IllegalArgumentException("Page cannot be negative");
		}

		if (size <= 0) {
			throw new IllegalArgumentException("Size must be greater than 0");
		}

		if (size > 100) {
			size = 100;
		}

		List<String> allowedSortFields = List.of("assignmentId", "createdOn",
				"status");

		if (!allowedSortFields.contains(sortBy)) {
			sortBy = "assignmentId";
		}

		Sort sort = sortDir.equalsIgnoreCase("desc")
				? Sort.by(sortBy).descending()
				: Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		// 🔥 Dynamic Logic
		Page<EmployeeAssignmentEntity> entityPage;

		if (employeeId != null) {
			entityPage = repository.findByEmployeeId(employeeId, pageable);
		} else {
			entityPage = repository.findAll(pageable);
		}

		Page<EmployeeAssignmentResponse> dtoPage = entityPage
				.map(this::mapToResponse);

		List<EmployeeAssignmentResponse> content = dtoPage.getContent();

		List<ItemIDResponse> itemIDResponses = validationService.getAllItems();

		List<ParamResponseDTO> categoryParamResponseDTOs = validationService
				.getParamByCodeAndSerial("Item", "Category");

		Map<Integer, String> itemIdToCategoryMap = itemIDResponses.stream()
				.collect(Collectors.toMap(ItemIDResponse::getItemId,
						ItemIDResponse::getCategory, (a, b) -> a

				));

		Map<String, String> categoryToParamMap = categoryParamResponseDTOs
				.stream().collect(Collectors.toMap(ParamResponseDTO::getDesp1,
						ParamResponseDTO::getDesp2, (a, b) -> a));

		List<ParamResponseDTO> typeParamResponseDTOs = validationService
				.getParamByCodeAndSerial("CONTRACT", "CONTRACT_TYPE");

		Map<String, String> typeToParamMap = typeParamResponseDTOs.stream()
				.collect(Collectors.toMap(ParamResponseDTO::getDesp1,
						ParamResponseDTO::getDesp2, (a, b) -> a));

		for (int i = 0; i < content.size(); i++) {

			EmployeeAssignmentResponse response = content.get(i);
			EmployeeAssignmentEntity entity = entityPage.getContent().get(i);

			ContractMaster contract = entity.getContract();

			if (contract != null && contract.getCustomer() != null) {

				response.setCustomerName(
						contract.getCustomer().getCustomerName());

				response.setCustomerId(contract.getCustomer().getCustomerId());

				HierarchyLevelResponseDTO responseDTO = validationService
						.validateAndGetHierarchyLevel(
								contract.getCustomer().getHierarchyLevelId());

				if (responseDTO != null) {
					response.setZoneName(responseDTO.getLevelName());
				}

				String paramType = typeToParamMap
						.get(contract.getContractType());

				response.setVisitType(paramType);

				List<ContractItemMapping> contractItemMappings = contractItemMappingRepository
						.findByContract_ContractId(contract.getContractId());

				List<String> productTypes = contractItemMappings.stream()
						.map(itemMapping -> {
							Integer itemId = itemMapping.getItemId();

							String category = itemIdToCategoryMap.get(itemId);

							if (category == null) {
								return null;
							}

							return categoryToParamMap.get(category);
						}).filter(Objects::nonNull).toList();

				response.setProductName(productTypes);

				List<CompanySiteMappingResponse> companySites = validationService
						.getAllCompSiteMappingByCompId(
								contract.getCustomer().getCompId());

				if (companySites != null && !companySites.isEmpty()) {

					Integer siteId = companySites.get(0).getSiteId();
					response.setSiteId(siteId);

					CompSiteResponse siteResponse = validationService
							.validateAndGetSite(siteId, "AB");

					if (siteResponse != null) {
						response.setSiteName(siteResponse.getSiteName());
						response.setIfsc(siteResponse.getIfsc());
						response.setDistrict(siteResponse.getDistrict());
					}
				}
			}
		}

		return new PageResponse<>(dtoPage.getContent(), dtoPage.getNumber(),
				dtoPage.getSize(), dtoPage.getTotalElements(),
				dtoPage.getTotalPages(), dtoPage.isLast());
	}

	private EmployeeAssignmentResponse mapToResponse(
			EmployeeAssignmentEntity entity) {

		EmployeeAssignmentResponse response = new EmployeeAssignmentResponse();

		response.setAssignmentId(entity.getAssignmentId());

		response.setContractId(entity.getContract() != null
				? entity.getContract().getContractId()
				: null);

		response.setEmployeeId(entity.getEmployeeId());

		EmployeeDTO employeeDTO = validationService
				.validateEmployeeExists(entity.getEmployeeId());
		response.setEmployeeName(employeeDTO.getEmployeeName());

		response.setSiteId(entity.getSiteId());

		response.setAssignmentStartDate(entity.getAssignmentStartDate() != null
				? entity.getAssignmentStartDate().toLocalDate()
				: null);

		response.setAssignmentEndDate(entity.getAssignmentEndDate() != null
				? entity.getAssignmentEndDate().toLocalDate()
				: null);

		response.setVisitDate(entity.getVisitDate() != null
				? entity.getVisitDate().toLocalDate()
				: null);

		response.setStatus(entity.getStatus());
		response.setRemark(entity.getRemark());

		response.setVisitType(entity.getContract().getContractType());

		response.setCreatedOn(entity.getCreatedOn());
		response.setModifiedOn(entity.getModifiedOn());

		response.setCreatedBy(entity.getCreatedBy());
		response.setModifiedBy(entity.getModifiedBy());

		return response;
	}

	@Transactional
	@Override
	public ResponseEntity getCustomerDetailsByAssignmentId(
			Integer assignmentId) {

		if (assignmentId == null) {
			throw new IllegalArgumentException(
					"Assignment ID must not be null");
		}

		try {

			EmployeeAssignmentEntity assignment = repository
					.findById(assignmentId)
					.orElseThrow(() -> new ResourceNotFoundException(
							"Assignment not found"));

			ContractMaster contract = assignment.getContract();
			if (contract == null) {
				throw new ResourceNotFoundException(
						"Contract not found for assignment");
			}

			CustomerMasterEntity customer = contract.getCustomer();
			if (customer == null) {
				throw new ResourceNotFoundException(
						"Customer not found for contract");
			}

			List<ContractEntityMapping> mappings = contractEntityMappingRepository
					.findByContract(contract);

			Integer minNoVisits = null;

			if (mappings != null && !mappings.isEmpty()) {
				minNoVisits = mappings.stream()
						.map(ContractEntityMapping::getMinNoVisits)
						.filter(Objects::nonNull).min(Integer::compareTo)
						.orElse(null);
			}

			CustomerResponse dto = new CustomerResponse();
			dto.setCustomerId(customer.getCustomerId());
			dto.setCustomerName(customer.getCustomerName());
			dto.setDistrict(customer.getDistrict());
			dto.setMinNoVisits(minNoVisits);

			return new ResponseEntity("Success", 200, dto);

		} catch (ResourceNotFoundException | IllegalArgumentException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to fetch customer details", ex);
		}
	}

}
