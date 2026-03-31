package com.doritech.EmployeeService.serviceImp;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doritech.EmployeeService.Mapper.CompanyMapper;
import com.doritech.EmployeeService.Specification.CompanySpecification;
import com.doritech.EmployeeService.entity.CompanyEntity;
import com.doritech.EmployeeService.exception.BusinessException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.CompanyRepository;
import com.doritech.EmployeeService.request.CompanyRequestDTO;
import com.doritech.EmployeeService.response.CompanyResponseDTO;
import com.doritech.EmployeeService.response.PageResponseDTO;
import com.doritech.EmployeeService.service.CompanyService;

import jakarta.transaction.Transactional;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository repo;

	@Autowired
	private CompanyMapper mapper;

	@Override
	public CompanyResponseDTO saveCompany(CompanyRequestDTO dto) {

		if (dto == null)
			throw new BusinessException("Company data must not be null");

		if (repo.existsByCompanyCode(dto.getCompanyCode()))
			throw new BusinessException("Company Code already exists");

		CompanyEntity entity = mapper.toEntity(dto);

		return mapper.toDTO(repo.save(entity));
	}

	@Override
	public CompanyResponseDTO updateCompany(Integer id, CompanyRequestDTO dto) {

		if (id == null || id <= 0)
			throw new BusinessException("Invalid company ID");

		CompanyEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Company not found with ID: " + id));

		if (dto.getEmail() != null
				&& repo.existsByCompanyCodeAndIdNot(dto.getCompanyCode(), id)) {
			throw new BusinessException(
					"Company Code already used by another company");
		}

		mapper.updateEntityFromDto(dto, entity);

		return mapper.toDTO(repo.save(entity));
	}

	@Override
	public CompanyResponseDTO getCompanyById(Integer id) {

		if (id == null || id <= 0)
			throw new BusinessException("Invalid company ID");

		CompanyEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Company not found with ID: " + id));

		return mapper.toDTO(entity);
	}

	@Override
	public void deleteById(Integer id) {

		if (id == null || id <= 0) {
			throw new BusinessException("Invalid company ID");
		}

		CompanyEntity entity = repo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Company not found with ID: " + id));

		repo.delete(entity);
	}

	@Override
	public PageResponseDTO<CompanyResponseDTO> getAllCompany(int page,
			int size) {

		if (page < 0)
			throw new BusinessException("Page number cannot be negative");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1 and 100");

		Page<CompanyResponseDTO> result = repo
				.findAll(PageRequest.of(page, size)).map(mapper::toDTO);

		return new PageResponseDTO<>(result);
	}

	@Override
	public PageResponseDTO<CompanyResponseDTO> companyfilter(String companyName,
			String companyCode, String contactPerson, String email,
			String phone, String city, String state, String country,
			String postalCode, String active, Long createdBy,
			LocalDate fromDate, LocalDate toDate, int page, int size) {

		if (page < 0)
			throw new BusinessException("Page number cannot be negative");

		if (size <= 0 || size > 100)
			throw new BusinessException("Page size must be between 1 and 100");

		Page<CompanyResponseDTO> page2 = repo.findAll(
				CompanySpecification.filter(companyName, companyCode,
						contactPerson, email, phone, city, state, country,
						postalCode, active, createdBy, fromDate, toDate),
				PageRequest.of(page, size)).map(mapper::toDTO);

		return new PageResponseDTO<>(page2);
	}

	@Override
	public List<CompanyResponseDTO> getByCompanyName(String name) {
		List<CompanyEntity> list = repo
				.findByCompanyNameContainingIgnoreCase(name);

		if (list.isEmpty()) {
			throw new ResourceNotFoundException(
					"No company found with name: " + name);
		}

		return list.stream().map(mapper::toDTO).toList();
	}

	@Override
	public CompanyResponseDTO getByCompanyCode(String code) {
		return mapper.toDTO(repo.findByCompanyCode(code)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Company not found with code: " + code)));
	}

	@Override
	public List<CompanyResponseDTO> getByCity(String city) {
		List<CompanyEntity> list = repo.findByCityIgnoreCase(city);

		if (list.isEmpty()) {
			throw new ResourceNotFoundException(
					"No company found in city: " + city);
		}

		return list.stream().map(mapper::toDTO).toList();
	}

	@Override
	public List<CompanyResponseDTO> getByPostalCode(String postalCode) {
		List<CompanyEntity> list = repo.findByPostalCode(postalCode);

		if (list.isEmpty()) {
			throw new ResourceNotFoundException(
					"No company found with postal code: " + postalCode);
		}

		return list.stream().map(mapper::toDTO).toList();
	}

	@Override
	public byte[] saveCompaniesFromExcel(MultipartFile file) {

		if (file.isEmpty())
			throw new BusinessException("Excel file is empty");

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream());
				Workbook resultWorkbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.getSheetAt(0);

			validateExcelHasData(sheet);

			Sheet resultSheet = resultWorkbook.createSheet("Upload Result");

			Row headerRow = sheet.getRow(0);
			Row resultHeader = resultSheet.createRow(0);

			int colCount = headerRow.getLastCellNum();

			for (int i = 0; i < colCount; i++) {
				resultHeader.createCell(i).setCellValue(
						headerRow.getCell(i).getStringCellValue());
			}

			resultHeader.createCell(colCount).setCellValue("Status");
			resultHeader.createCell(colCount + 1).setCellValue("Remark");

			Map<String, Integer> headerMap = new HashMap<>();
			for (Cell cell : headerRow) {
				headerMap.put(cell.getStringCellValue().trim(),
						cell.getColumnIndex());
			}

			Set<String> excelCodes = new HashSet<>();
			List<CompanyEntity> validCompanies = new ArrayList<>();

			int resultRowNum = 1;

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);
				if (row == null)
					continue;

				Row resultRow = resultSheet.createRow(resultRowNum++);

				for (int j = 0; j < colCount; j++) {
					Cell c = row.getCell(j);
					if (c != null)
						resultRow.createCell(j).setCellValue(getCellValue(c));
				}

				String remark = null;

				String companyName = getCell(row, headerMap, "Company Name");
				String companyCode = getCell(row, headerMap, "Company Code");
				String email = getCell(row, headerMap, "Email");
				String phone = getCell(row, headerMap, "Phone");
				String createdBy = getCell(row, headerMap, "Created By");

				if (companyName == null || companyName.isBlank())
					remark = "Company Name missing";

				else if (companyCode == null || companyCode.isBlank())
					remark = "Company Code missing";

				else if (!excelCodes.add(companyCode))
					remark = "Duplicate code in Excel";

				else if (repo.existsByCompanyCode(companyCode))
					remark = "Company code already exists";

				else if (email != null
						&& !email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
					remark = "Invalid email";

				else if (phone != null && phone.length() < 10)
					remark = "Invalid phone";

				else {
					try {
						Integer createdById = Integer.parseInt(createdBy);

						CompanyEntity company = new CompanyEntity();
						company.setCompanyName(companyName);
						company.setCompanyCode(companyCode);
						company.setContactPerson(
								getCell(row, headerMap, "Contact Person"));
						company.setEmail(email);
						company.setPhone(phone);
						company.setAddress(getCell(row, headerMap, "Address"));
						company.setCity(getCell(row, headerMap, "City"));
						company.setState(getCell(row, headerMap, "State"));
						company.setCountry(getCell(row, headerMap, "Country"));
						company.setPostalCode(
								getCell(row, headerMap, "Postal Code"));
						company.setActive(
								getCell(row, headerMap, "Active (Y/N)"));
						company.setCreatedBy(createdById);

						validCompanies.add(company);

					} catch (Exception e) {
						remark = "Invalid CreatedBy";
					}
				}

				if (remark == null) {
					resultRow.createCell(colCount).setCellValue("Saved");
					resultRow.createCell(colCount + 1).setCellValue("Success");
				} else {
					resultRow.createCell(colCount).setCellValue("Failed");
					resultRow.createCell(colCount + 1).setCellValue(remark);
				}
			}

			if (!validCompanies.isEmpty())
				repo.saveAll(validCompanies);

			resultWorkbook.write(out);
			return out.toByteArray();

		} catch (Exception e) {
			throw new BusinessException(
					"Excel processing failed: " + e.getMessage());
		}
	}

	private void validateExcelHasData(Sheet sheet) {

		boolean hasData = false;

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row != null && row.getPhysicalNumberOfCells() > 0) {
				hasData = true;
				break;
			}
		}

		if (!hasData)
			throw new BusinessException("No data found in Excel to upload.");
	}

	private String getCell(Row row, Map<String, Integer> headerMap,
			String header) {

		Integer index = headerMap.get(header);
		if (index == null)
			return null;

		Cell cell = row.getCell(index);
		return getCellValue(cell);
	}

	private String getCellValue(Cell cell) {

		if (cell == null)
			return null;

		switch (cell.getCellType()) {
			case STRING :
				return cell.getStringCellValue().trim();
			case NUMERIC :
				return String.valueOf((long) cell.getNumericCellValue());
			case BOOLEAN :
				return String.valueOf(cell.getBooleanCellValue());
			default :
				return null;
		}
	}

	@Override
	@Transactional
	public void deleteMultiple(List<Integer> ids) {

		List<CompanyEntity> entities = repo.findAllById(ids);

		if (entities.isEmpty()) {
			throw new ResourceNotFoundException(
					"No company found for given IDs");
		}

		repo.deleteAll(entities);
	}

	@Override
	public List<CompanyResponseDTO> getAllActiveCompanies() {

		return repo.findByActive("Y").stream().map(entity -> {
			CompanyResponseDTO dto = new CompanyResponseDTO();
			dto.setId(entity.getId());
			dto.setCompanyName(entity.getCompanyName());
			dto.setCompanyCode(entity.getCompanyCode());
			return dto;
		}).toList();
	}

	@Override
	public boolean existCompanyCode(String companyCode) {
		return repo.existsByCompanyCode(companyCode);
	}

}