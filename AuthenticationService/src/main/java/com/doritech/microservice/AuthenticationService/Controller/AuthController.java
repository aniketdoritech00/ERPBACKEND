package com.doritech.microservice.AuthenticationService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.microservice.AuthenticationService.Entity.ResponseEntity;
import com.doritech.microservice.AuthenticationService.Request.LoginRequest;
import com.doritech.microservice.AuthenticationService.Service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity login(@Valid @RequestBody LoginRequest request) {
		System.out.println("Login request received for email: ");
		System.out.println("Testing");
		System.out.println("Login request received for email: " );
		return authService.login(request);
	}

	@GetMapping("/health")
	public ResponseEntity health() {
		return new ResponseEntity("Auth Service running", 200, null);
	}
}