package com.doritech.EmployeeService.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.exception.ApiResponse;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.request.CompanyRequestDTO;
import com.doritech.EmployeeService.service.CompanyService;
import com.doritech.EmployeeService.service.OnCreate;
import com.doritech.EmployeeService.service.OnUpdate;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee/api/company")
public class CompanyController {

	@Autowired
	private CompanyService service;

	@PostMapping("/saveCompany")
	public ResponseEntity saveCompany(
			@Validated(OnCreate.class) @RequestBody CompanyRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setCreatedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Company saved successfully");
		response.setData(service.saveCompany(dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Company saved successfully",
				HttpStatus.OK.value(), response);
	}

	@PutMapping("/updateCompany/{id}")
	public ResponseEntity updateCompany(@PathVariable Integer id,
			@Validated(OnUpdate.class) @RequestBody CompanyRequestDTO dto,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		dto.setModifiedBy(Integer.parseInt(userId));

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Company updated successfully");
		response.setData(service.updateCompany(id, dto));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Company updated successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getCompanyById/{id}")
	public ResponseEntity getCompanyById(@PathVariable Integer id,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Company fetched successfully");
		response.setData(service.getCompanyById(id));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Company fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/getAllCompany")
	public ResponseEntity getAllCompany(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Companies fetched successfully");
		response.setData(service.getAllCompany(page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Companies fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity deleteById(@PathVariable Integer id,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		System.out.println("User id is" + userId);

		service.deleteById(id);
		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Company deleted successfully");
		response.setData(null);
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Company deleted successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/companyfilter")
	public ResponseEntity companyfilter(
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false) String companyCode,
			@RequestParam(required = false) String contactPerson,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String phone,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String state,
			@RequestParam(required = false) String country,
			@RequestParam(required = false) String postalCode,
			@RequestParam(required = false) String active,
			@RequestParam(required = false) Long createdBy,
			@RequestParam(required = false) LocalDate fromDate,
			@RequestParam(required = false) LocalDate toDate,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Filtered companies fetched successfully");
		response.setData(service.companyfilter(companyName, companyCode,
				contactPerson, email, phone, city, state, country, postalCode,
				active, createdBy, fromDate, toDate, page, size));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Filtered companies fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@PostMapping("/upload-excel")
	public org.springframework.http.ResponseEntity<byte[]> uploadExcel(
			@RequestParam("file") MultipartFile file) {

		byte[] excelResult = service.saveCompaniesFromExcel(file);

		return org.springframework.http.ResponseEntity.ok()
				.header("Content-Disposition",
						"attachment; filename=Uploaded_Result.xlsx")
				.header("Content-Type",
						"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
				.body(excelResult);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity getByName(@PathVariable String name,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Companies fetched by name");
		response.setData(service.getByCompanyName(name));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Companies fetched by name",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/code/{code}")
	public ResponseEntity getByCode(@PathVariable String code,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Company fetched by code");
		response.setData(service.getByCompanyCode(code));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Company fetched by code",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/city/{city}")
	public ResponseEntity getByCity(@PathVariable String city,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Companies fetched by city");
		response.setData(service.getByCity(city));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Companies fetched by city",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/postal/{postalCode}")
	public ResponseEntity getByPostalCode(@PathVariable String postalCode,
			HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Companies fetched by postal code");
		response.setData(service.getByPostalCode(postalCode));
		response.setStatusCode(200);
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Companies fetched by postal code",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/activeCompanies")
	public ResponseEntity getAllActiveCompanies(HttpServletRequest request) {

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Active organizations fetched successfully");
		response.setData(service.getAllActiveCompanies());
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Active organizations fetched successfully",
				HttpStatus.OK.value(), response);
	}

	@DeleteMapping("/deleteMultipleCompanys")
	public ResponseEntity deleteMultipleCompanys(@RequestBody List<Integer> ids,
			@RequestHeader("X-User-Id") String userId,
			HttpServletRequest request) {

		if (ids == null || ids.isEmpty()) {
			throw new BusinessException("ID list cannot be empty");
		}

		System.out.println("User id is" + userId);

		service.deleteMultiple(ids);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setMessage("Organizations deleted successfully");
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		return new ResponseEntity("Organizations deleted successfully",
				HttpStatus.OK.value(), response);
	}

	@GetMapping("/existCompanyCode")
	public ResponseEntity existCompanyCode(@RequestParam String companyCode,
			HttpServletRequest request) {

		boolean exists = service.existCompanyCode(companyCode);

		ApiResponse<Object> response = new ApiResponse<>();
		response.setSuccess(true);
		response.setData(exists);
		response.setStatusCode(HttpStatus.OK.value());
		response.setPath(request.getRequestURI());

		if (exists) {
			response.setMessage("Company code exists");
			return new ResponseEntity("Company code exists",
					HttpStatus.OK.value(), response);
		} else {
			response.setMessage("Company code does not exist");
			return new ResponseEntity("Company code does not exist",
					HttpStatus.OK.value(), response);
		}
	}

}
