package com.doritech.CustomerService.ServiceImpl;

import com.doritech.CustomerService.Repository.ContractInstallationDetailsRepository;
import java.time.LocalDateTime;
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
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.EmployeeAssignmentEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.ContractEntityMappingRepository;
import com.doritech.CustomerService.Repository.ContractItemMappingRepository;
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
public class EmployeeAssignmentServiceImpl implements EmployeeAssignmentService {

	private final ContractInstallationDetailsRepository contractInstallationDetailsRepository;

    @Autowired
	private EmployeeAssignmentRepository repository;

	@Autowired
	private ContractItemMappingRepository contractItemMappingRepository;
	@Autowired
	private ContractEntityMappingRepository contractEntityMappingRepository;

	@Autowired
	private ValidationService validationService;

    EmployeeAssignmentServiceImpl(ContractInstallationDetailsRepository contractInstallationDetailsRepository) {
        this.contractInstallationDetailsRepository = contractInstallationDetailsRepository;
    }

	@Override
	@Transactional
	public EmployeeAssignmentResponse saveEmployeeAssignment(EmployeeAssignmentRequest request) {

		EmployeeAssignmentEntity entity = new EmployeeAssignmentEntity();

		Optional<ContractEntityMapping> contractEntityMapping = contractEntityMappingRepository
				.findById(request.getMappingId());

		if (contractEntityMapping.isEmpty()) {
			throw new ResourceNotFoundException("Contract not Found with this ID " + request.getMappingId());
		}

		if (contractEntityMapping.get().getContract().getAmcType() == "IN") {
			entity.setHelperId(request.getHelperId());
		}

		entity.setContractEntityMapping(contractEntityMapping.get());
		entity.setEmployeeId(request.getEmployeeId());
		entity.setSiteId(request.getSiteId());
		entity.setAssignmentStartDate(request.getAssignmentStartDate());
		entity.setAssignmentEndDate(request.getAssignmentEndDate());
		entity.setVisitType(contractEntityMapping.get().getContract().getContractType());
		entity.setVerifyStatus("Pending");
		entity.setVerifyOn(null);
		entity.setVisitDate(request.getVisitDate());
		entity.setStatus("Pending");
		entity.setRemark("NA");
		entity.setCreatedBy(request.getCreatedBy());

		EmployeeAssignmentEntity saved = repository.save(entity);

		return mapToResponse(saved);
	}

	@Override
	@Transactional
	public EmployeeAssignmentResponse updateEmployeeAssignmentStatus(Integer assignmentId,
			EmployeeAssignmentRequest request) {

		EmployeeAssignmentEntity entity = repository.findById(assignmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID " + assignmentId));

		Optional<ContractEntityMapping> contractEntityMapping = contractEntityMappingRepository
				.findById(request.getMappingId());

		if (contractEntityMapping.isEmpty()) {
			throw new ResourceNotFoundException("Contract Mapping not Found with this ID " + request.getMappingId());
		}

		entity.setStatus(request.getStatus());
		entity.setRemark(request.getRemark());
		entity.setModifiedBy(request.getModifiedBy());

		EmployeeAssignmentEntity saved = repository.save(entity);

		return mapToResponse(saved);
	}

	@Override
	@Transactional
	public List<EmployeeAssignmentResponse> saveBulkEmployeeAssignment(List<EmployeeAssignmentRequest> requests) {

		if (requests == null || requests.isEmpty()) {
			throw new ResourceNotFoundException("Request list cannot be empty");
		}

		Set<Integer> contractIds = requests.stream().map(EmployeeAssignmentRequest::getMappingId)
				.collect(Collectors.toSet());

		Map<Integer, ContractEntityMapping> contractMap = contractEntityMappingRepository.findAllById(contractIds)
				.stream().collect(Collectors.toMap(ContractEntityMapping::getMappingId, c -> c));

		List<EmployeeAssignmentEntity> entities = new ArrayList<>();

		for (EmployeeAssignmentRequest request : requests) {

			ContractEntityMapping contractEntityMapping = contractMap.get(request.getMappingId());

			if (contractEntityMapping == null) {
				throw new ResourceNotFoundException("Contract not found with ID " + request.getMappingId());
			}

			EmployeeAssignmentEntity entity = new EmployeeAssignmentEntity();

			entity.setContractEntityMapping(contractEntityMapping);
			entity.setEmployeeId(request.getEmployeeId());
			entity.setSiteId(request.getSiteId());
			entity.setAssignmentStartDate(request.getAssignmentStartDate());
			entity.setAssignmentEndDate(request.getAssignmentEndDate());
			entity.setVisitDate(request.getVisitDate());
			if (contractEntityMapping.getContract().getAmcType() == "IN") {
				entity.setHelperId(request.getHelperId());
			}
			entity.setVisitType(contractEntityMapping.getContract().getContractType());
			entity.setVerifyStatus("Pending");
			entity.setVerifyOn(null);
			entity.setStatus("Pending");
			entity.setRemark("NA");
			entity.setCreatedBy(request.getCreatedBy());

			entities.add(entity);
		}

		List<EmployeeAssignmentEntity> savedEntities = repository.saveAll(entities);

		return savedEntities.stream().map(this::mapToResponse).toList();
	}

	@Override
	public PageResponse<EmployeeAssignmentResponse> getEmployeeAssignments(Integer employeeId, int page, int size,
			String sortBy, String sortDir) {

		if (page < 0) {
			throw new IllegalArgumentException("Page cannot be negative");
		}

		if (size <= 0) {
			throw new IllegalArgumentException("Size must be greater than 0");
		}

		if (size > 100) {
			size = 100;
		}

		List<String> allowedSortFields = List.of("assignmentId", "createdOn", "status");

		if (!allowedSortFields.contains(sortBy)) {
			sortBy = "assignmentId";
		}

		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		Page<EmployeeAssignmentEntity> entityPage;

		if (employeeId != null) {
			entityPage = repository.findByEmployeeId(employeeId, pageable);
		} else {
			entityPage = repository.findAll(pageable);
		}

		Page<EmployeeAssignmentResponse> dtoPage = entityPage.map(this::mapToResponse);

		List<EmployeeAssignmentResponse> content = dtoPage.getContent();

		List<ItemIDResponse> itemIDResponses = validationService.getAllItems();

		List<ParamResponseDTO> categoryParamResponseDTOs = validationService.getParamByCodeAndSerial("Item",
				"Category");

		Map<Integer, String> itemIdToCategoryMap = itemIDResponses.stream()
				.collect(Collectors.toMap(ItemIDResponse::getItemId, ItemIDResponse::getCategory, (a, b) -> a

				));

		Map<String, String> categoryToParamMap = categoryParamResponseDTOs.stream()
				.collect(Collectors.toMap(ParamResponseDTO::getDesp1, ParamResponseDTO::getDesp2, (a, b) -> a));

		List<ParamResponseDTO> typeParamResponseDTOs = validationService.getParamByCodeAndSerial("CONTRACT",
				"CONTRACT_TYPE");

		Map<String, String> typeToParamMap = typeParamResponseDTOs.stream()
				.collect(Collectors.toMap(ParamResponseDTO::getDesp1, ParamResponseDTO::getDesp2, (a, b) -> a));

		for (int i = 0; i < content.size(); i++) {

			EmployeeAssignmentResponse response = content.get(i);
			EmployeeAssignmentEntity entity = entityPage.getContent().get(i);

			ContractEntityMapping contractEntityMapping = entity.getContractEntityMapping();

			if (contractEntityMapping != null && contractEntityMapping.getCustomer() != null) {

				response.setCustomerName(contractEntityMapping.getCustomer().getCustomerName());

				response.setCustomerId(contractEntityMapping.getCustomer().getCustomerId());

				HierarchyLevelResponseDTO responseDTO = validationService
						.validateAndGetHierarchyLevel(contractEntityMapping.getCustomer().getHierarchyLevelId());

				if (responseDTO != null) {
					response.setZoneName(responseDTO.getLevelName());
				}

				String paramType = typeToParamMap.get(contractEntityMapping.getContract().getContractType());

				response.setVisitType(paramType);

				List<ContractItemMapping> contractItemMappings = contractItemMappingRepository
						.findByContract_ContractId(contractEntityMapping.getContract().getContractId());

				List<String> productTypes = contractItemMappings.stream().map(itemMapping -> {
					Integer itemId = itemMapping.getItemId();

					String category = itemIdToCategoryMap.get(itemId);

					if (category == null) {
						return null;
					}

					return categoryToParamMap.get(category);
				}).filter(Objects::nonNull).distinct().toList();

				response.setSalesOrderNo(contractInstallationDetailsRepository.findByContractContractId(contractEntityMapping.getContract().getContractId()).get().getSalesOrderNumber());

				response.setProductName(productTypes);

				response.setIfsc(contractEntityMapping.getCustomer().getIfsc());

				List<CompanySiteMappingResponse> companySites = validationService
						.getAllCompSiteMappingByCompId(contractEntityMapping.getCustomer().getCompId());

				if (companySites != null && !companySites.isEmpty()) {

					Integer siteId = companySites.get(0).getSiteId();
					response.setSiteId(siteId);

					CompSiteResponse siteResponse = validationService.validateAndGetSite(siteId, "AB");

					if (siteResponse != null) {
						response.setSiteName(siteResponse.getSiteName());

						response.setDistrict(siteResponse.getDistrict());
					}
				}
			}
		}

		return new PageResponse<>(dtoPage.getContent(), dtoPage.getNumber(), dtoPage.getSize(),
				dtoPage.getTotalElements(), dtoPage.getTotalPages(), dtoPage.isLast());
	}

	private EmployeeAssignmentResponse mapToResponse(EmployeeAssignmentEntity entity) {

		EmployeeAssignmentResponse response = new EmployeeAssignmentResponse();

		response.setAssignmentId(entity.getAssignmentId());

		response.setMappingId(
				entity.getContractEntityMapping() != null && entity.getContractEntityMapping().getContract() != null
						? entity.getContractEntityMapping().getContract().getContractId()
						: null);

		response.setEmployeeId(entity.getEmployeeId());

		EmployeeDTO employeeDTO = validationService.validateEmployeeExists(entity.getEmployeeId());
		response.setEmployeeName(employeeDTO.getEmployeeName());

		response.setHelperId(entity.getHelperId());

		if (entity.getHelperId() != null) {
			EmployeeDTO helperDTO = validationService.validateEmployeeExists(entity.getHelperId());
			response.setHelperName(helperDTO.getEmployeeName());
		}

		response.setSiteId(entity.getSiteId());

		response.setIfsc(entity.getContractEntityMapping().getContract().getCustomer().getIfsc());

		response.setAssignmentStartDate(
				entity.getAssignmentStartDate() != null ? entity.getAssignmentStartDate().toLocalDate() : null);

		response.setAssignmentEndDate(
				entity.getAssignmentEndDate() != null ? entity.getAssignmentEndDate().toLocalDate() : null);

		response.setVisitDate(entity.getVisitDate() != null ? entity.getVisitDate().toLocalDate() : null);

		response.setStatus(entity.getStatus());
		response.setRemark(entity.getRemark());

		response.setVerifyStatus(entity.getVerifyStatus());
		response.setVerifyBy(entity.getVerifyBy());
		if(entity.getVerifyBy() != null) {
			EmployeeDTO verifyByDTO = validationService.validateEmployeeExists(entity.getVerifyBy());
			response.setVerifyByName(verifyByDTO.getEmployeeName());
		}
		response.setVerifyOn(entity.getVerifyOn());

		response.setVisitType(entity.getVisitType() != null
						? entity.getVisitType()
						: null);

		response.setCreatedOn(entity.getCreatedOn());
		response.setModifiedOn(entity.getModifiedOn());

		response.setCreatedBy(entity.getCreatedBy());
		response.setModifiedBy(entity.getModifiedBy());

		return response;
	}

	@Transactional
	@Override
	public ResponseEntity getCustomerDetailsByAssignmentId(Integer assignmentId) {

		if (assignmentId == null) {
			throw new IllegalArgumentException("Assignment ID must not be null");
		}

		try {

			EmployeeAssignmentEntity assignment = repository.findById(assignmentId)
					.orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

			ContractEntityMapping contractEntityMapping = assignment.getContractEntityMapping();
			if (contractEntityMapping == null) {
				throw new ResourceNotFoundException("Contract not found for assignment");
			}

			CustomerMasterEntity customer = contractEntityMapping.getCustomer();
			if (customer == null) {
				throw new ResourceNotFoundException("Customer not found for contract");
			}

			List<ContractEntityMapping> mappings = contractEntityMappingRepository
					.findByContract(contractEntityMapping.getContract());

			Integer minNoVisits = null;

			if (mappings != null && !mappings.isEmpty()) {
				minNoVisits = mappings.stream().map(ContractEntityMapping::getMinNoVisits).filter(Objects::nonNull)
						.min(Integer::compareTo).orElse(null);
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

	@Override
	@Transactional
	public ResponseEntity updateStatusAfterPdfGenerate(Integer assignmentId) {

		Optional<EmployeeAssignmentEntity> optional = repository.findById(assignmentId);

		if (optional.isEmpty()) {
			return new ResponseEntity("Assignment not found", 404, null);
		}
		EmployeeAssignmentEntity assignment = optional.get();
		assignment.setStatus("Completed");
		assignment.setModifiedOn(LocalDateTime.now());

		repository.save(assignment);

		return new ResponseEntity("Status updated successfully", 200, null);
	}

	@Override
@Transactional
public ResponseEntity updateVerifyStatus(Integer assignmentId, String verifyStatus, String verifyRemark, Integer userId) {

    Optional<EmployeeAssignmentEntity> optional = repository.findById(assignmentId);

    if (optional.isEmpty()) {
        return new ResponseEntity("Assignment not found", 404, null);
    }

    EmployeeAssignmentEntity assignment = optional.get();

    String currentStatus = assignment.getVerifyStatus();

    if (currentStatus != null &&
        (currentStatus.equalsIgnoreCase("VERIFIED") || currentStatus.equalsIgnoreCase("REJECTED"))) {

        return new ResponseEntity("This assignment is already " + currentStatus.toLowerCase(), 409, null);
    }

    if (!"VERIFIED".equalsIgnoreCase(verifyStatus) &&
        !"REJECTED".equalsIgnoreCase(verifyStatus)) {

        return new ResponseEntity("Invalid verify status. Allowed: VERIFIED or REJECTED", 400, null);
    }

    assignment.setVerifyStatus(verifyStatus.toUpperCase());
    assignment.setVerifyOn(LocalDateTime.now());
    assignment.setVerifyBy(userId);
    assignment.setVerifyRemark(verifyRemark);
    assignment.setModifiedOn(LocalDateTime.now());
    assignment.setModifiedBy(userId);

    repository.save(assignment);

    return new ResponseEntity("Verify status updated successfully", 200, null);
}

	@Override
	@Transactional
	public List<EmployeeAssignmentResponse> getAssignmentByIds(List<Integer> assignmentIds) {
		List<EmployeeAssignmentEntity> assignmentEntities = repository.findAllById(assignmentIds);

		if(assignmentEntities == null || assignmentEntities.isEmpty()) {
			throw new ResourceNotFoundException("No assignments found for the provided IDs");
		}

		List<EmployeeAssignmentResponse> responses = new ArrayList<>();
		for(EmployeeAssignmentEntity entity : assignmentEntities) {

			EmployeeAssignmentResponse response = new EmployeeAssignmentResponse();
			response.setAssignmentId(entity.getAssignmentId());
			response.setEmployeeId(entity.getEmployeeId());
			response.setHelperId(entity.getHelperId());
			response.setVisitType(entity.getVisitType());
			response.setVerifyStatus(entity.getVerifyStatus());
			response.setVerifyRemark(entity.getVerifyRemark());
			response.setStatus(entity.getStatus());
			responses.add(response);
		}

		return responses;
	}
}
