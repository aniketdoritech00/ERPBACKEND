package com.doritech.EmployeeService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doritech.EmployeeService.entity.ResponseEntity;
import com.doritech.EmployeeService.request.PasswordPolicyRequest;
import com.doritech.EmployeeService.request.UserMasterRequest;
import com.doritech.EmployeeService.service.PasswordPolicyService;

@RestController
@RequestMapping("employee/api/password-policy")
public class PasswordPolicyController {

    private final PasswordPolicyService service;

    public PasswordPolicyController(PasswordPolicyService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity savePolicy(
            @RequestBody PasswordPolicyRequest request,
            @RequestHeader("X-User-Id") String userId) {

        return new ResponseEntity("Password Policy saved successfully",HttpStatus.OK.value(),
                service.savePolicy(request, Integer.parseInt(userId))
        );
    }

    @PostMapping("/validate")
    public ResponseEntity validatePassword(
            @RequestParam String password,@RequestHeader("X-User-Id") String userId) {

        return new ResponseEntity("Password is valid",HttpStatus.OK.value(),
                service.validatePassword(password,Integer.parseInt(userId))
        );
    }
}