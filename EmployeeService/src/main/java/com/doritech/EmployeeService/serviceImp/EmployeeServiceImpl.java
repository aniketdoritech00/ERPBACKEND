package com.doritech.EmployeeService.serviceImp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.Specification.EmployeeSpecification;
import com.doritech.EmployeeService.entity.EmployeeMaster;
import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.exception.DuplicateResourceException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.CompSiteMasterRepository;
import com.doritech.EmployeeService.repository.CompanyRepository;
import com.doritech.EmployeeService.repository.EmployeeMasterRepository;
import com.doritech.EmployeeService.request.EmployeeRequest;
import com.doritech.EmployeeService.response.EmployeeResponse;
import com.doritech.EmployeeService.response.ParamResponseDTO;
import com.doritech.EmployeeService.service.EmployeeService;
import com.doritech.EmployeeService.service.ParamService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMasterRepository employeeMasterRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompSiteMasterRepository siteRepository;
	@Autowired
	private ParamService paramService;

	@Override
	public EmployeeResponse saveEmployee(EmployeeRequest request) {
		if (employeeMasterRepository
				.existsByEmployeeCode(request.getEmployeeCode())) {
			throw new DuplicateResourceException(
					"Employee code already exists");
		}

		EmployeeMaster employee = mapToEntity(request);
		employee.setCreatedOn(LocalDateTime.now());

		EmployeeMaster saved = employeeMasterRepository.save(employee);
		return mapToResponse(saved);
	}

	@Override
	public EmployeeResponse updateEmployee(Integer employeeId,
			EmployeeRequest request) {
		EmployeeMaster existing = employeeMasterRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Employee not found with id: " + employeeId));

		if (!existing.getEmployeeCode().equals(request.getEmployeeCode())
				&& employeeMasterRepository
						.existsByEmployeeCode(request.getEmployeeCode())) {
			throw new DuplicateResourceException(
					"Employee code already exists");
		}

		existing.setEmployeeName(request.getEmployeeName());
		existing.setEmployeeCode(request.getEmployeeCode());
		existing.setEmail(request.getEmail());
		existing.setPhone(request.getPhone());
		existing.setDesignation(request.getDesignation());
		existing.setDepartment(request.getDepartment());
		existing.setRole(request.getRole());
		existing.setAddress(request.getAddress());
		existing.setParent(request.getParentId());
		existing.setCity(request.getCity());
		existing.setDistrict(request.getDistrict());
		existing.setState(request.getState());
		existing.setCountry(request.getCountry());
		existing.setPostalCode(request.getPostalCode());
		existing.setIsActive(request.getIsActive());
		existing.setDateOfJoining(request.getDateOfJoining());
		existing.setDateOfLeaving(request.getDateOfLeaving());
		existing.setModifiedOn(LocalDateTime.now());
		existing.setModifiedBy(request.getModifiedBy());

		existing.setCompany(companyRepository.findById(request.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Company not found")));
		existing.setSite(
				siteRepository.findById(request.getSiteId()).orElseThrow(
						() -> new ResourceNotFoundException("Site not found")));

		EmployeeMaster updated = employeeMasterRepository.save(existing);
		return mapToResponse(updated);
	}

	@Override
	public EmployeeResponse getEmployeeById(Integer employeeId) {
		EmployeeMaster employee = employeeMasterRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Employee not found with id: " + employeeId));
		return mapToResponse(employee);
	}

	@Override
	public List<EmployeeResponse> getAllEmployees() {
		List<EmployeeMaster> list = employeeMasterRepository.findAll();
		return list.stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public Page<EmployeeResponse> getEmployeesWithPagination(int page,
			int size) {
		Pageable pageable = PageRequest.of(page, size,
				Sort.by("createdOn").descending());
		Page<EmployeeMaster> employeePage = employeeMasterRepository
				.findAll(pageable);
		return employeePage.map(this::mapToResponse);
	}

	@Override
	public Page<EmployeeResponse> filterEmployees(String employeeName,
			String employeeCode, String isActive, String email, String phone,
			String department, String designation, String role,
			Integer companyId, Integer siteId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size,
				Sort.by("createdOn").descending());

		Page<EmployeeMaster> employeePage = employeeMasterRepository
				.findAll(EmployeeSpecification.filter(employeeName,
						employeeCode, isActive, email, phone, department,
						designation, role, companyId, siteId), pageable);

		return employeePage.map(this::mapToResponse);
	}

	private EmployeeMaster mapToEntity(EmployeeRequest request) {
		EmployeeMaster emp = new EmployeeMaster();

		emp.setEmployeeName(request.getEmployeeName());
		emp.setEmployeeCode(request.getEmployeeCode());
		emp.setEmail(request.getEmail());
		emp.setPhone(request.getPhone());
		emp.setDesignation(request.getDesignation());
		emp.setDepartment(request.getDepartment());
		emp.setRole(request.getRole());
		emp.setAddress(request.getAddress());
		emp.setCity(request.getCity());
		emp.setDistrict(request.getDistrict());
		emp.setState(request.getState());
		emp.setCountry(request.getCountry());
		emp.setPostalCode(request.getPostalCode());
		emp.setIsActive(request.getIsActive());
		emp.setDateOfJoining(request.getDateOfJoining());
		emp.setDateOfLeaving(request.getDateOfLeaving());
		emp.setCreatedBy(request.getCreatedBy());

		if (request.getParentId() == 0) {
			emp.setParent(0);
		} else {
			if (!employeeMasterRepository.existsById(request.getParentId())) {
				throw new ResourceNotFoundException(
						"Parent employee not found");
			}
			emp.setParent(request.getParentId());
		}

		emp.setCompany(companyRepository.findById(request.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Company not found")));
		emp.setSite(siteRepository.findById(request.getSiteId()).orElseThrow(
				() -> new ResourceNotFoundException("Site not found")));

		return emp;
	}

	private EmployeeResponse mapToResponse(EmployeeMaster emp) {
		EmployeeResponse resp = new EmployeeResponse();

		resp.setEmployeeId(emp.getEmployeeId());
		resp.setEmployeeName(emp.getEmployeeName());
		resp.setEmployeeCode(emp.getEmployeeCode());
		resp.setEmail(emp.getEmail());
		resp.setPhone(emp.getPhone());
		resp.setAddress(emp.getAddress());
		resp.setCity(emp.getCity());
		resp.setDistrict(emp.getDistrict());
		resp.setState(emp.getState());
		resp.setCountry(emp.getCountry());
		resp.setPostalCode(emp.getPostalCode());
		resp.setDateOfJoining(emp.getDateOfJoining());
		resp.setDateOfLeaving(emp.getDateOfLeaving());
		resp.setCreatedBy(emp.getCreatedBy());
		resp.setModifiedBy(emp.getModifiedBy());
		resp.setCreatedOn(emp.getCreatedOn());
		resp.setModifiedOn(emp.getModifiedOn());

		if (emp.getCompany() != null) {
			resp.setCompanyId(emp.getCompany().getId());
			resp.setCompanyName(emp.getCompany().getCompanyName());
			resp.setCompanyCode(emp.getCompany().getCompanyCode());
		}

		if (emp.getSite() != null) {
			resp.setSiteId(emp.getSite().getSiteId());
			resp.setSiteName(emp.getSite().getSiteName());
			resp.setSiteCode(emp.getSite().getSiteCode());
		}

		if (emp.getParent() != null && emp.getParent() != 0) {
			EmployeeMaster parent = employeeMasterRepository
					.findById(emp.getParent()).orElse(null);
			if (parent != null) {
				resp.setParentId(parent.getEmployeeId());
				resp.setParentName(parent.getEmployeeName());
			}
		}

		// Map desp2 values using ParamService
		resp.setRole(mapDesp2(emp.getRole(), "EMPLOYEE", "ROLE"));
		resp.setDesignation(
				mapDesp2(emp.getDesignation(), "EMPLOYEE", "DESIGNATION"));
		resp.setDepartment(
				mapDesp2(emp.getDepartment(), "EMPLOYEE", "DEPARTMENT"));
		resp.setIsActive(mapDesp2(emp.getIsActive(), "EMPLOYEE", "STATUS"));

		return resp;
	}

	@SuppressWarnings("unchecked")
	private String mapDesp2(String empValue, String code, String serial) {
		if (empValue == null)
			return null;

		try {
			List<ParamResponseDTO> params = (List<ParamResponseDTO>) paramService
					.getByCodeAndSerial(code, serial);

			for (ParamResponseDTO p : params) {
				if (p.getDesp1() != null
						&& p.getDesp1().equalsIgnoreCase(empValue)) {
					return p.getDesp2();
				}
			}

			return empValue;
		} catch (ResourceNotFoundException e) {
			return empValue;
		}
	}

	public ResponseEntity getCompanyIdAndName() {

		List<Object[]> companies = companyRepository.findCompanyIdAndName();

		if (companies.isEmpty()) {
			throw new ResourceNotFoundException("No active companies found");
		}

		List<Map<String, Object>> list = new ArrayList<>();

		for (Object[] obj : companies) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", obj[0]);
			map.put("companyName", obj[1]);
			map.put("companyCode", obj[2]);
			list.add(map);
		}

		return new ResponseEntity("Company list fetched successfully", 200,
				list);
	}

	// ✅ Site Id & Name
	public ResponseEntity getSiteIdAndName() {

		List<Object[]> sites = siteRepository.findSiteIdAndName();

		if (sites.isEmpty()) {
			throw new ResourceNotFoundException("No active sites found");
		}

		List<Map<String, Object>> list = new ArrayList<>();

		for (Object[] obj : sites) {
			Map<String, Object> map = new HashMap<>();
			map.put("siteId", obj[0]);
			map.put("siteName", obj[1]);
			map.put("siteCode", obj[2]);
			list.add(map);
		}

		return new ResponseEntity("Site list fetched successfully", 200, list);
	}

	@Override
	public List<Map<String, Object>> getEmployeeAsParentList() {

		List<EmployeeMaster> employees = employeeMasterRepository
				.findByIsActive("Y");

		if (employees.isEmpty()) {
			throw new ResourceNotFoundException(
					"No active Parent employees found");
		}

		List<Map<String, Object>> list = new ArrayList<>();

		for (EmployeeMaster emp : employees) {
			Map<String, Object> map = new HashMap<>();
			map.put("parentId", emp.getEmployeeId());
			map.put("parentName", emp.getEmployeeName());
			map.put("parentCode", emp.getEmployeeCode());
			list.add(map);
		}

		return list;
	}

	@Override
	public ResponseEntity getAllActiveEmployees() {

		List<EmployeeMaster> employees = employeeMasterRepository
				.findByIsActive("Y");

		List<Map<String, Object>> result = new ArrayList<>();

		for (EmployeeMaster emp : employees) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeId", emp.getEmployeeId());
			map.put("employeeName", emp.getEmployeeName());
			map.put("employeeCode", emp.getEmployeeCode());
			result.add(map);
		}

		if (result.isEmpty()) {
			return new ResponseEntity("No employees found", 404, null);
		}

		return new ResponseEntity("Employees fetched successfully", 200,
				result);
	}

	@Override
	public ResponseEntity getAllAssociateFa(Integer siteId) {
		if (siteId == null) {
			throw new BusinessException("site id can not be null");

		}
		List<EmployeeMaster> employeeMasters = employeeMasterRepository
				.findBySite_SiteIdAndDesignationAndIsActive(siteId, "FA", "Y");
		if (employeeMasters.isEmpty()) {
			throw new ResourceNotFoundException("Employee not found");

		}
		List<Map<String, Object>> list = new ArrayList<>();
		for (EmployeeMaster emp : employeeMasters) {
			Map<String, Object> map = new HashMap<>();
			map.put("employeeId", emp.getEmployeeId());
			map.put("employeeCode", emp.getEmployeeCode());
			map.put("employeeName", emp.getEmployeeName());
			list.add(map);

		}
		return new ResponseEntity("employee found successfully", 200, list);
	}

	public ResponseEntity getEmployeeDistrict(Integer employeeId) {
		if (employeeId == null) {
			return new ResponseEntity("Employee ID must not be null", 400,
					null);
		}

		EmployeeMaster employee = employeeMasterRepository.findById(employeeId)
				.orElse(null);
		if (employee == null) {
			return new ResponseEntity("Employee not found", 404, null);
		}

		String district = employee.getDistrict();
		if (district == null || district.isEmpty()) {
			return new ResponseEntity("District not found for this employee",
					404, null);
		}
		return new ResponseEntity("District fetched successfully", 200,
				district);
	}
}