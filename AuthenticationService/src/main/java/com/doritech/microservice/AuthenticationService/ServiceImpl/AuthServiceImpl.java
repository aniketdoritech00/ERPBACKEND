package com.doritech.microservice.AuthenticationService.ServiceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doritech.microservice.AuthenticationService.Entity.ResponseEntity;
import com.doritech.microservice.AuthenticationService.Entity.UserMaster;
import com.doritech.microservice.AuthenticationService.Exception.InvalidCredentialsException;
import com.doritech.microservice.AuthenticationService.Repository.EmployeeMasterRepository;
import com.doritech.microservice.AuthenticationService.Repository.UserMasterRepository;
import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Service.AuthService;
import com.doritech.microservice.AuthenticationService.Service.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserMasterRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private EmployeeMasterRepository employeeMasterRepository;

	@Override
	public ResponseEntity login(LoginRequest request) {

		UserMaster user = userRepo.findByLoginId(request.getLoginId())
				.orElseThrow(() -> new InvalidCredentialsException("Login failed. Invalid credentials"));
		String combined = request.getLoginId() + request.getPassword();
		if (!passwordEncoder.matches(combined, user.getPassword())) {
			throw new InvalidCredentialsException("Login failed. Invalid credentials");
		}

		Integer employeeId = user.getSourceId();

		String employeeName = null;

		if (employeeId != null) {
			employeeName = employeeMasterRepository.findByEmployeeId(employeeId).map(emp -> emp.getEmployeeName()).orElse(null);
		}

		String token = jwtService.generateTokenWithUserData(user, employeeName);

		user.setLastLogin(LocalDateTime.now());
		userRepo.save(user);
		return new ResponseEntity("Login successful", 200, token);
	}
}