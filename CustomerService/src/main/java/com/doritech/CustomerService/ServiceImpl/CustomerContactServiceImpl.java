package com.doritech.CustomerService.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doritech.CustomerService.Entity.CustomerContactEntity;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.BadRequestException;
import com.doritech.CustomerService.Exception.InternalServerException;
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

	public CustomerContactServiceImpl(CustomerContactRepository repository) {
		this.repository = repository;
	}

	@Autowired
	private CustomerMasterRepository customerMasterRepository;

	@Override
	@Transactional
	public ResponseEntity saveCustomerContact(List<CustomerContactRequest> requests) {

		logger.info("Save/Update Customer Contact API called");

		if (requests == null || requests.isEmpty())
			throw new BadRequestException("Request body cannot be null or empty");

		int saveCount = 0;
		int updateCount = 0;

		for (CustomerContactRequest request : requests) {

			if (request.getCustomerId() == null)
				throw new BadRequestException("CustomerId is required");

			if (!customerMasterRepository.existsById(request.getCustomerId()))
				throw new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId());

			CustomerContactEntity entity;

			if (request.getCustContId() != null) {

				entity = repository.findById(request.getCustContId()).orElseThrow(() -> new ResourceNotFoundException(
						"Customer Contact not found with id: " + request.getCustContId()));

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

			} else {

				if (request.getCreatedBy() == null)
					throw new BadRequestException("CreatedBy is required");

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
			}
		}

		logger.info("Customer Contacts Saved: {}, Updated: {}", saveCount, updateCount);

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
	public ResponseEntity getAllCustomerContacts() {

		logger.info("Get All Customer Contacts API called");

		try {

			List<CustomerContactEntity> contacts = repository.findAll();

			if (contacts.isEmpty()) {
				logger.warn("No Customer Contacts found");
				return new ResponseEntity("No Customer Contacts Found", 404, null);
			}

			List<CustomerContactResponse> responseList = new ArrayList<>();

			for (CustomerContactEntity entity : contacts) {

				CustomerContactResponse response = new CustomerContactResponse();

				response.setCustContId(entity.getCustContId());
				response.setCustomerId(entity.getCustomerId());
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

				responseList.add(response);
			}

			logger.info("Customer Contacts fetched successfully");

			return new ResponseEntity("Success", 200, responseList);

		} catch (Exception ex) {
			logger.error("Error while fetching Customer Contacts", ex);
			throw new InternalServerException("Something went wrong while fetching Customer Contacts");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity getCustomerContacts(Integer customerId) {

		logger.info("Get Customer Contacts {}", customerId);

		if (customerId == null)
			throw new BadRequestException("CustomerId Cannot Be Null");

		List<CustomerContactEntity> list = repository.findByCustomerId(customerId);

		if (list == null || list.isEmpty())
			throw new ResourceNotFoundException("Customer Contacts Not Found");

		List<CustomerContactResponse> response = list.stream().map(c -> {

			CustomerContactResponse r = new CustomerContactResponse();

			r.setCustContId(c.getCustContId());
			r.setContactPerson(c.getContactPerson());
			r.setEmail(c.getEmail());
			r.setPhone(c.getPhone());
			r.setDesignation(c.getDesignation());
			r.setRole(c.getRole());
			r.setDepartment(c.getDepartment());
			r.setIsActive(c.getIsActive());

			return r;

		}).toList();

		return new ResponseEntity("Success", 200, response);
	}
}