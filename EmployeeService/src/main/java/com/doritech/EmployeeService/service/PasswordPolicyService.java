package com.doritech.EmployeeService.service;

import com.doritech.EmployeeService.request.PasswordPolicyRequest;
import com.doritech.EmployeeService.response.PasswordPolicyResponse;

public interface PasswordPolicyService {

    PasswordPolicyResponse savePolicy(PasswordPolicyRequest request, Integer userId);

    String validatePassword(String password, Integer userId);
}