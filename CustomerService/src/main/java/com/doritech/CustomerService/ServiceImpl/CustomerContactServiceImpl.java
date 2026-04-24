package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.CustomerService.Entity.CustomerContactEntity;
import com.doritech.CustomerService.Entity.CustomerMasterEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.CustomerContactRepository;
import com.doritech.CustomerService.Repository.CustomerMasterRepository;
import com.doritech.CustomerService.Request.CustomerContactRequest;
import com.doritech.CustomerService.Response.CustomerContactResponse;
import com.doritech.CustomerService.Service.CustomerContactService;

@Service
public class CustomerContactServiceImpl implements CustomerContactService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerContactServiceImpl.class);

	private final CustomerContactRepository repository;

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	public CustomerContactServiceImpl(CustomerContactRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public ResponseEntity saveCustomerContact(List<CustomerContactRequest> requests) {

		logger.info("saveCustomerContact API called with {} records", requests != null ? requests.size() : 0);

		if (requests == null || requests.isEmpty()) {
			logger.error("saveCustomerContact failed: Request body is null or empty");
			throw new BadRequestException("Request body cannot be null or empty");
		}

		int saveCount = 0;
		int updateCount = 0;

		for (CustomerContactRequest request : requests) {

			if (request.getCustomerId() == null) {
				logger.error("saveCustomerContact failed: CustomerId is null");
				throw new BadRequestException("CustomerId is required");
			}

			if (!customerMasterRepository.existsById(request.getCustomerId())) {
				logger.error("saveCustomerContact failed: Customer not found with id: {}", request.getCustomerId());
				throw new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId());
			}

			CustomerContactEntity entity;

			if (request.getCustContId() != null) {

				logger.info("Updating Customer Contact with id: {}", request.getCustContId());

				entity = repository.findById(request.getCustContId()).orElseThrow(() -> {
					logger.error("saveCustomerContact failed: Customer Contact not found with id: {}",
							request.getCustContId());
					return new ResourceNotFoundException(
							"Customer Contact not found with id: " + request.getCustContId());
				});

				entity.setContactPerson(request.getContactPerson());
				entity.setEmail(request.getEmail());
				entity.setPhone(request.getPhone());
				entity.setDesignation(request.getDesignation());
				entity.setRole(request.getRole());
				entity.setDepartment(request.getDepartment());
				entity.setIsActive(request.getIsActive());
				entity.setModifiedBy(request.getCreatedBy());
				entity.setModifiedOn(LocalDateTime.now());

				repository.save(entity);
				updateCount++;

				logger.info("Customer Contact updated successfully with id: {}", request.getCustContId());

			} else {

				if (request.getCreatedBy() == null) {
					logger.error("saveCustomerContact failed: CreatedBy is null for new contact");
					throw new BadRequestException("CreatedBy is required");
				}

				logger.info("Saving new Customer Contact for customerId: {}", request.getCustomerId());

				entity = new CustomerContactEntity();

				entity.setCustomerId(request.getCustomerId());
				entity.setContactPerson(request.getContactPerson());
				entity.setEmail(request.getEmail());
				entity.setPhone(request.getPhone());
				entity.setDesignation(request.getDesignation());
				entity.setRole(request.getRole());
				entity.setDepartment(request.getDepartment());
				entity.setIsActive(request.getIsActive() == null ? "Y" : request.getIsActive());
				entity.setCreatedBy(request.getCreatedBy());
				entity.setCreatedOn(LocalDateTime.now());

				repository.save(entity);
				saveCount++;

				logger.info("Customer Contact saved successfully for customerId: {}", request.getCustomerId());
			}
		}

		logger.info("saveCustomerContact completed - Saved: {}, Updated: {}", saveCount, updateCount);

		String message;
		if (saveCount > 0 && updateCount > 0) {
			message = "Customer Contacts Saved and Updated Successfully";
		} else if (saveCount > 0) {
			message = "Customer Contacts Saved Successfully";
		} else {
			message = "Customer Contacts Updated Successfully";
		}

		return new ResponseEntity(message, 200, null);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getAllCustomerContacts(int page, int size) {

		logger.info("getAllCustomerContacts API called with page: {}, size: {}", page, size);

		if (page < 0) {
			logger.error("getAllCustomerContacts failed: Page index must not be less than zero");
			throw new BadRequestException("Page index must not be less than zero");
		}

		if (size <= 0) {
			logger.error("getAllCustomerContacts failed: Page size must not be less than one");
			throw new BadRequestException("Page size must not be less than one");
		}

		Pageable pageable = PageRequest.of(page, size);
		Page<CustomerContactEntity> contactPage = repository.findAll(pageable);

		if (contactPage.isEmpty()) {
			logger.warn("getAllCustomerContacts: No Customer Contacts found");
			throw new ResourceNotFoundException("No Customer Contacts Found");
		}

		List<CustomerContactResponse> responseList = contactPage.getContent().stream().map(entity -> {

			CustomerContactResponse response = new CustomerContactResponse();

			response.setCustContId(entity.getCustContId());
			response.setCustomerId(entity.getCustomerId());

			if (entity.getCustomerId() != null) {
				CustomerMasterEntity customerMasterEntity = customerMasterRepository
						.findByCustomerId(entity.getCustomerId());

				if (customerMasterEntity != null) {
					response.setCustomerCode(customerMasterEntity.getCustomerCode());
					response.setCustomerName(customerMasterEntity.getCustomerName());
				} else {
					logger.warn("CustomerMaster not found for customerId: {}", entity.getCustomerId());
				}
			}

			response.setContactPerson(entity.getContactPerson());
			response.setEmail(entity.getEmail());
			response.setPhone(entity.getPhone());
			response.setDesignation(entity.getDesignation());
			response.setRole(entity.getRole());
			response.setDepartment(entity.getDepartment());
			response.setIsActive(entity.getIsActive());
			response.setCreatedBy(entity.getCreatedBy());
			response.setCreatedOn(entity.getCreatedOn());
			response.setModifiedBy(entity.getModifiedBy());
			response.setModifiedOn(entity.getModifiedOn());

			return response;

		}).toList();

		Map<String, Object> payload = new HashMap<>();
		payload.put("data", responseList);
		payload.put("currentPage", contactPage.getNumber());
		payload.put("pageSize", contactPage.getSize());
		payload.put("totalElements", contactPage.getTotalElements());
		payload.put("totalPages", contactPage.getTotalPages());
		payload.put("isLast", contactPage.isLast());

		logger.info("getAllCustomerContacts: Fetched {} records on page {}/{}", responseList.size(), page + 1,
				contactPage.getTotalPages());

		return new ResponseEntity("Success", 200, payload);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getCustomerContacts(Integer customerId) {

		logger.info("getCustomerContacts API called for customerId: {}", customerId);

		if (customerId == null) {
			logger.error("getCustomerContacts failed: CustomerId is null");
			throw new BadRequestException("CustomerId Cannot Be Null");
		}

		if (!customerMasterRepository.existsById(customerId)) {
			logger.error("getCustomerContacts failed: Customer not found with CustomerId: {}", customerId);
			throw new ResourceNotFoundException("Customer not found with CustomerId: " + customerId);
		}

		List<CustomerContactEntity> list = repository.findByCustomerId(customerId);

		if (list == null || list.isEmpty()) {
			logger.warn("getCustomerContacts: No contacts found for customerId: {}", customerId);
			throw new ResourceNotFoundException("Customer Contacts Not Found for customerId: " + customerId);
		}

		List<CustomerContactResponse> response = list.stream().map(c -> {

			CustomerContactResponse r = new CustomerContactResponse();

			r.setCustContId(c.getCustContId());
			r.setCustomerId(c.getCustomerId());

			if (c.getCustomerId() != null) {
				CustomerMasterEntity customerMasterEntity = customerMasterRepository
						.findByCustomerId(c.getCustomerId());

				if (customerMasterEntity != null) {
					r.setCustomerCode(customerMasterEntity.getCustomerCode());
					r.setCustomerName(customerMasterEntity.getCustomerName());
				} else {
					logger.warn("getCustomerContacts: CustomerMaster not found for customerId: {}", c.getCustomerId());
				}
			}

			r.setContactPerson(c.getContactPerson());
			r.setEmail(c.getEmail());
			r.setPhone(c.getPhone());
			r.setDesignation(c.getDesignation());
			r.setRole(c.getRole());
			r.setDepartment(c.getDepartment());
			r.setIsActive(c.getIsActive());
			r.setCreatedBy(c.getCreatedBy());
			r.setCreatedOn(c.getCreatedOn());
			r.setModifiedBy(c.getModifiedBy());
			r.setModifiedOn(c.getModifiedOn());

			return r;

		}).toList();

		logger.info("getCustomerContacts: Fetched {} contacts for customerId: {}", response.size(), customerId);

		return new ResponseEntity("Success", 200, response);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getCustomerContactsDetails(Integer contactId) {

		logger.info("getCustomerContactsDetails API called for contactId: {}", contactId);

		if (contactId == null) {
			logger.error("getCustomerContactsDetails failed: ContactId is null");
			throw new BadRequestException("ContactId cannot be null");
		}

		CustomerContactEntity c = repository.findById(contactId).orElseThrow(() -> {
			logger.error("getCustomerContactsDetails failed: Customer Contact not found for contactId: {}", contactId);
			return new ResourceNotFoundException("Customer Contact not found for contactId: " + contactId);
		});

		CustomerContactResponse payload = new CustomerContactResponse();
		payload.setCustContId(c.getCustContId());
		payload.setCustomerId(c.getCustomerId());

		if (c.getCustomerId() != null) {
			CustomerMasterEntity customerMasterEntity = customerMasterRepository.findByCustomerId(c.getCustomerId());

			if (customerMasterEntity != null) {
				payload.setCustomerCode(customerMasterEntity.getCustomerCode());
				payload.setCustomerName(customerMasterEntity.getCustomerName());
			} else {
				logger.warn("getCustomerContactsDetails: CustomerMaster not found for customerId: {}",
						c.getCustomerId());
			}
		}

		payload.setContactPerson(c.getContactPerson());
		payload.setEmail(c.getEmail());
		payload.setPhone(c.getPhone());
		payload.setDesignation(c.getDesignation());
		payload.setRole(c.getRole());
		payload.setDepartment(c.getDepartment());
		payload.setIsActive(c.getIsActive());
		payload.setCreatedBy(c.getCreatedBy());
		payload.setCreatedOn(c.getCreatedOn());
		payload.setModifiedBy(c.getModifiedBy());
		payload.setModifiedOn(c.getModifiedOn());

		logger.info("getCustomerContactsDetails: Contact fetched successfully for contactId: {}", contactId);

		return new ResponseEntity("Customer Contact fetched successfully", 200, payload);
	}

	@Override
	@Transactional
	public ResponseEntity deleteBulkCustomerContact(List<Integer> contactIds) {

		logger.info("Delete Bulk CustomerContact API called for ids: {}", contactIds);

		if (contactIds == null || contactIds.isEmpty()) {
			logger.error("Delete Bulk CustomerContact failed: Contact IDs are null or empty");
			throw new BadRequestException("Contact IDs cannot be null or empty");
		}

		List<CustomerContactEntity> contacts = repository.findAllById(contactIds);

		if (contacts.isEmpty()) {
			logger.error("Delete Bulk CustomerContact failed: No contacts found for ids: {}", contactIds);
			throw new ResourceNotFoundException("No customer contacts found for given IDs");
		}

		repository.deleteAll(contacts);

		logger.info("Delete Bulk CustomerContact: {} records deleted successfully for ids: {}", contacts.size(),
				contactIds);

		return new ResponseEntity("Customer contacts deleted successfully", 200, null);
	}
}