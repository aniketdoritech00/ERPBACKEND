package com.doritech.CustomerService.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.doritech.CustomerService.Entity.EmployeeAssignmentEntity;
import com.doritech.CustomerService.Entity.HDDConfig;
import com.doritech.CustomerService.Entity.Installation;
import com.doritech.CustomerService.Entity.InstallationImage;
import com.doritech.CustomerService.Entity.ResponseEntity;
import com.doritech.CustomerService.Exception.ResourceNotFoundException;
import com.doritech.CustomerService.Repository.EmployeeAssignmentRepository;
import com.doritech.CustomerService.Repository.HDDConfigRepository;
import com.doritech.CustomerService.Repository.InstallationImageRepository;
import com.doritech.CustomerService.Repository.InstallationRepository;
import com.doritech.CustomerService.Request.InstallationRequest;
import com.doritech.CustomerService.Response.InstallationResponse;
import com.doritech.CustomerService.Response.PageResponse;
import com.doritech.CustomerService.Service.FileStorageService;
import com.doritech.CustomerService.Service.installationService;

import jakarta.transaction.Transactional;

@Service
public class InstallationServiceImpl implements installationService {

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private InstallationRepository installationRepository;

	@Autowired
	private InstallationImageRepository installationImageRepository;

	@Autowired
	private HDDConfigRepository hddConfigRepository;

	@Autowired
	private EmployeeAssignmentRepository assignmentRepository;

	@Override
	@Transactional
	public ResponseEntity saveInstallation(InstallationRequest request, MultipartFile hddImage,
			MultipartFile deviceImage, List<MultipartFile> serviceImages, Integer userId) throws Exception {
		// System.out.println("App Running Dir: " + System.getProperty("user.dir"));

		Installation installation = new Installation();

		installation.setBranch(request.getBranch());
		installation.setAssignmentId(request.getAssignmentId());
		installation.setSalesOrder(request.getSalesOrder());
		installation.setDcAdvBill(request.getDcAdvBill());

		installation.setWiring(request.getWiring());
		installation.setMounting(request.getMounting());
		installation.setCommissioning(request.getCommissioning());
		installation.setFinalDemo(request.getFinalDemo());

		installation.setPvcPipe(request.getPvcPipe());
		installation.setPvcBend(request.getPvcBend());
		installation.setExternalAssistant(request.getExternalAssistant());
		installation.setExternalHelperAadhar(request.getExternalHelperAadhar());

		installation.setReportNo(request.getReportNo());
		installation.setRemarks(request.getRemarks());
		installation.setCreatedBy(userId);
		installation = installationRepository.save(installation);

		HDDConfig hddConfig = new HDDConfig();
		hddConfig.setSize1TB(request.getSize1TB());
		hddConfig.setSize2TB(request.getSize2TB());
		hddConfig.setSize4TB(request.getSize4TB());
		hddConfig.setSize6TB(request.getSize6TB());
		hddConfig.setSize8TB(request.getSize8TB());
		hddConfig.setSize10TB(request.getSize10TB());
		int totalSize = Optional.ofNullable(request.getSize1TB()).orElse(0)
				+ Optional.ofNullable(request.getSize2TB()).orElse(0)
				+ Optional.ofNullable(request.getSize4TB()).orElse(0)
				+ Optional.ofNullable(request.getSize6TB()).orElse(0)
				+ Optional.ofNullable(request.getSize8TB()).orElse(0)
				+ Optional.ofNullable(request.getSize10TB()).orElse(0);

		hddConfig.setTotalSize(totalSize);
		hddConfig.setInstallation(installation);

		hddConfigRepository.save(hddConfig);

		List<InstallationImage> imageList = new ArrayList<>();

		if (hddImage != null && !hddImage.isEmpty()) {
			validateFile(hddImage);
			String fileName = fileStorageService.saveFile(hddImage);
			// System.out.println("Saved HDD: " + fileName);

			InstallationImage img = new InstallationImage();
			img.setImageType("HDD_CONFIG");
			img.setFileName(fileName);
			img.setFilePath("uploads/" + fileName);
			img.setInstallation(installation);

			imageList.add(img);
		}

		if (deviceImage != null && !deviceImage.isEmpty()) {
			validateFile(deviceImage);
			String fileName = fileStorageService.saveFile(deviceImage);
			// System.out.println("Saved Device: " + fileName);

			InstallationImage img = new InstallationImage();
			img.setImageType("DEVICE_INFO");
			img.setFileName(fileName);
			img.setFilePath("uploads/" + fileName);
			img.setInstallation(installation);

			imageList.add(img);
		}

		if (serviceImages != null && !serviceImages.isEmpty()) {
			for (MultipartFile file : serviceImages) {
				validateFile(file);
				String fileName = fileStorageService.saveFile(file);
				// System.out.println("Saved Service Report: " + fileName);

				InstallationImage img = new InstallationImage();
				img.setImageType("SERVICE_REPORT");
				img.setFileName(fileName);
				img.setFilePath("uploads/" + fileName);
				img.setInstallation(installation);

				imageList.add(img);
			}
		}

		installationImageRepository.saveAll(imageList);

		return new ResponseEntity("Installation Details Saved Successfully", HttpStatus.OK.value(), null);
	}

	@Override
	@Transactional
	public ResponseEntity saveInstallationForFasAndSas(InstallationRequest request, List<MultipartFile> serviceImages,
			Integer userId) throws Exception {
		// System.out.println("App Running Dir: " + System.getProperty("user.dir"));

		Installation installation = new Installation();

		installation.setBranch(request.getBranch());
		installation.setAssignmentId(request.getAssignmentId());
		installation.setSalesOrder(request.getSalesOrder());
		installation.setDcAdvBill(request.getDcAdvBill());

		installation.setWiring(request.getWiring());
		installation.setMounting(request.getMounting());
		installation.setCommissioning(request.getCommissioning());
		installation.setFinalDemo(request.getFinalDemo());

		installation.setPvcPipe(request.getPvcPipe());
		installation.setPvcBend(request.getPvcBend());
		installation.setExternalAssistant(request.getExternalAssistant());
		installation.setExternalHelperAadhar(request.getExternalHelperAadhar());

		installation.setReportNo(request.getReportNo());
		installation.setRemarks(request.getRemarks());
		installation.setCreatedBy(userId);
		installation = installationRepository.save(installation);

		List<InstallationImage> imageList = new ArrayList<>();

		// Service Report Images (Multiple)
		if (serviceImages != null && !serviceImages.isEmpty()) {
			for (MultipartFile file : serviceImages) {
				validateFile(file);
				String fileName = fileStorageService.saveFile(file);
				// System.out.println("Saved Service Report: " + fileName);

				InstallationImage img = new InstallationImage();
				img.setImageType("SERVICE_REPORT");
				img.setFileName(fileName);
				img.setFilePath("uploads/" + fileName);
				img.setInstallation(installation);

				imageList.add(img);
			}
		}

		installationImageRepository.saveAll(imageList);

		return new ResponseEntity("Installation Details for FAS/SAS Saved Successfully", HttpStatus.OK.value(), null);
	}

	@Override
	public ResponseEntity getInstallation(Long id) {
		InstallationResponse res = new InstallationResponse();

		Installation installation = installationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Installation not found with id: " + id));

		EmployeeAssignmentEntity assignmentEntity = assignmentRepository.findById(installation.getAssignmentId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Assignment not found with id: " + installation.getAssignmentId()));

		if (installation.getAssignmentId() != null && "IN".equalsIgnoreCase(assignmentEntity.getVisitType())) {

			HDDConfig hddConfig = hddConfigRepository.findByInstallation(installation)
					.orElseThrow(() -> new ResourceNotFoundException(
							"HDD Config not found for installation id: " + installation.getId()));

			res.setSize1TB(hddConfig.getSize1TB());
			res.setSize2TB(hddConfig.getSize2TB());
			res.setSize4TB(hddConfig.getSize4TB());
			res.setSize6TB(hddConfig.getSize6TB());
			res.setSize8TB(hddConfig.getSize8TB());
			res.setSize10TB(hddConfig.getSize10TB());
		}

		res.setId(installation.getId());
		res.setBranch(installation.getBranch());
		res.setAssignmentId(installation.getAssignmentId());
		res.setSalesOrder(installation.getSalesOrder());
		res.setVisitType(assignmentEntity.getVisitType());

		res.setWiring(installation.getWiring());
		res.setMounting(installation.getMounting());
		res.setCommissioning(installation.getCommissioning());
		res.setFinalDemo(installation.getFinalDemo());

		res.setPvcPipe(installation.getPvcPipe());
		res.setPvcBend(installation.getPvcBend());
		res.setExternalAssistant(installation.getExternalAssistant());
		res.setExternalHelperAadhar(installation.getExternalHelperAadhar());

		res.setReportNo(installation.getReportNo());
		res.setRemarks(installation.getRemarks());

		List<String> hdd = new ArrayList<>();
		List<String> device = new ArrayList<>();
		List<String> service = new ArrayList<>();

		String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

		for (InstallationImage img : installation.getImages()) {

			String fullUrl = baseUrl + "/" + img.getFilePath();

			if ("HDD_CONFIG".equals(img.getImageType())) {
				hdd.add(fullUrl);
			} else if ("DEVICE_INFO".equals(img.getImageType())) {
				device.add(fullUrl);
			} else {
				service.add(fullUrl);
			}
		}

		res.setHddImages(hdd);
		res.setDeviceImages(device);
		res.setServiceImages(service);

		return new ResponseEntity("Installation Details Retrieved Successfully", HttpStatus.OK.value(), res);
	}

	@Override
	public ResponseEntity getInstallationByAssignmentId(Integer assignmentId) {

		InstallationResponse res = new InstallationResponse();

		Installation installation = installationRepository.findByAssignmentId(assignmentId).orElseThrow(
				() -> new ResourceNotFoundException("Installation not found with assignment id: " + assignmentId));

		EmployeeAssignmentEntity assignmentEntity = assignmentRepository.findById(installation.getAssignmentId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Assignment not found with id: " + installation.getAssignmentId()));

		if (installation.getAssignmentId() != null && "IN".equalsIgnoreCase(assignmentEntity.getVisitType())) {

			HDDConfig hddConfig = hddConfigRepository.findByInstallation(installation)
					.orElseThrow(() -> new ResourceNotFoundException(
							"HDD Config not found for installation id: " + installation.getId()));

			res.setSize1TB(hddConfig.getSize1TB());
			res.setSize2TB(hddConfig.getSize2TB());
			res.setSize4TB(hddConfig.getSize4TB());
			res.setSize6TB(hddConfig.getSize6TB());
			res.setSize8TB(hddConfig.getSize8TB());
			res.setSize10TB(hddConfig.getSize10TB());
		}

		res.setId(installation.getId());
		res.setBranch(installation.getBranch());
		res.setAssignmentId(installation.getAssignmentId());
		res.setSalesOrder(installation.getSalesOrder());
		res.setVisitType(assignmentEntity.getVisitType());

		res.setWiring(installation.getWiring());
		res.setMounting(installation.getMounting());
		res.setCommissioning(installation.getCommissioning());
		res.setFinalDemo(installation.getFinalDemo());

		res.setPvcPipe(installation.getPvcPipe());
		res.setPvcBend(installation.getPvcBend());
		res.setExternalAssistant(installation.getExternalAssistant());
		res.setExternalHelperAadhar(installation.getExternalHelperAadhar());

		res.setReportNo(installation.getReportNo());
		res.setRemarks(installation.getRemarks());

		List<String> hdd = new ArrayList<>();
		List<String> device = new ArrayList<>();
		List<String> service = new ArrayList<>();

		String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

		for (InstallationImage img : installation.getImages()) {

			String fullUrl = baseUrl + "/" + img.getFilePath();

			if ("HDD_CONFIG".equals(img.getImageType())) {
				hdd.add(fullUrl);
			} else if ("DEVICE_INFO".equals(img.getImageType())) {
				device.add(fullUrl);
			} else {
				service.add(fullUrl);
			}
		}

		res.setHddImages(hdd);
		res.setDeviceImages(device);
		res.setServiceImages(service);

		return new ResponseEntity("Installation Details Retrieved Successfully", HttpStatus.OK.value(), res);
	}

	private void validateFile(MultipartFile file) {

		if (file.getSize() > 5 * 1024 * 1024) {
			throw new RuntimeException("File size should be less than 5MB");
		}

		String contentType = file.getContentType();

		if (!contentType.equals("image/png") && !contentType.equals("image/jpeg") && !contentType.equals("image/jpg")
				&& !contentType.equals("image/svg+xml")) {

			throw new RuntimeException("Only image files are allowed");
		}
	}

	@Override
	public ResponseEntity getAllInstallations(Integer page, Integer size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Installation> installationPage = installationRepository.findAll(pageable);

		List<InstallationResponse> installationResponses = installationPage.getContent().stream()
				.map(this::convertToResponse).collect(Collectors.toList());

		PageResponse<InstallationResponse> pageResponse = new PageResponse<>();
		pageResponse.setContent(installationResponses);
		pageResponse.setPageNumber(installationPage.getNumber());
		pageResponse.setPageSize(installationPage.getSize());
		pageResponse.setTotalElements(installationPage.getTotalElements());
		pageResponse.setTotalPages(installationPage.getTotalPages());
		pageResponse.setLastPage(installationPage.isLast());

		return new ResponseEntity("All Installations Retrieved Successfully", HttpStatus.OK.value(), pageResponse);
	}

	private InstallationResponse convertToResponse(Installation installation) {
		InstallationResponse res = new InstallationResponse();

		res.setId(installation.getId());
		res.setBranch(installation.getBranch());
		res.setAssignmentId(installation.getAssignmentId());
		res.setSalesOrder(installation.getSalesOrder());

		res.setWiring(installation.getWiring());
		res.setMounting(installation.getMounting());
		res.setCommissioning(installation.getCommissioning());
		res.setFinalDemo(installation.getFinalDemo());

		res.setPvcPipe(installation.getPvcPipe());
		res.setPvcBend(installation.getPvcBend());
		res.setExternalAssistant(installation.getExternalAssistant());
		res.setExternalHelperAadhar(installation.getExternalHelperAadhar());

		res.setReportNo(installation.getReportNo());
		res.setRemarks(installation.getRemarks());

		return res;

	}
}
