package com.doritech.EmployeeService.serviceImp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.doritech.EmployeeService.entity.EmployeeMaster;
import com.doritech.EmployeeService.entity.PasswordPolicy;
import com.doritech.EmployeeService.entity.UserMaster;
import com.doritech.EmployeeService.exception.PasswordPolicyException;
import com.doritech.EmployeeService.exception.ResourceNotFoundException;
import com.doritech.EmployeeService.repository.EmployeeMasterRepository;
import com.doritech.EmployeeService.repository.PasswordPolicyRepository;
import com.doritech.EmployeeService.repository.UserMasterRepository;
import com.doritech.EmployeeService.request.PasswordPolicyRequest;
import com.doritech.EmployeeService.request.UserMasterRequest;
import com.doritech.EmployeeService.response.PasswordPolicyResponse;
import com.doritech.EmployeeService.service.PasswordPolicyService;

@Service
public class PasswordPolicyServiceImpl implements PasswordPolicyService {

    private final PasswordPolicyRepository repo;
    private final EmployeeMasterRepository empRepo;
    private final UserMasterRepository userRepo;

    public PasswordPolicyServiceImpl(PasswordPolicyRepository repo,EmployeeMasterRepository empRepo,UserMasterRepository userRepo) {
        this.repo = repo;
        this.empRepo = empRepo;
        this.userRepo = userRepo;
    }

    @Override
    public PasswordPolicyResponse savePolicy(PasswordPolicyRequest request, Integer userId) {

        PasswordPolicy entity = new PasswordPolicy();

        BeanUtils.copyProperties(request, entity);

        entity.setCreatedBy(userId);

        // repo.findByStatus("Y").ifPresent(p -> {
        //     p.setStatus("N");
        //     repo.save(p);
        // });

        PasswordPolicy saved = repo.save(entity);

        return new PasswordPolicyResponse(
                saved.getId(),
                saved.getName(),
                saved.getMinLength(),
                saved.getMaxLength(),
                saved.getStatus());
    }

    @Override
    public String validatePassword(String password, Integer userId) {

        PasswordPolicy policy = repo.findByStatus("Y")
                .orElseThrow(() -> new PasswordPolicyException(
                        Map.of("policy", "Active password policy not found")));

        Map<String, String> errors = new HashMap<>();

        if (password == null || password.isBlank()) {
            throw new PasswordPolicyException(
                    Map.of("password", "Password cannot be empty"));
        }

        password = password.trim();

        System.out.println("Validating password: " + password);
        System.out.println("User Id is : " + userId);

        UserMaster user = userRepo.findById(userId)
                .orElseThrow(() -> new PasswordPolicyException(
                        Map.of("user", "User not found")));

        EmployeeMaster emp = empRepo.findById(user.getSourceId()).orElseThrow(() -> new PasswordPolicyException(
                Map.of("employee", "Employee not found")));

        // Length
        if (policy.getMinLength() != null && password.length() < policy.getMinLength()) {
            errors.put("minLength", "Minimum length is " + policy.getMinLength());
        }

        if (policy.getMaxLength() != null && password.length() > policy.getMaxLength()) {
            errors.put("maxLength", "Maximum length is " + policy.getMaxLength());
        }

        // Count chars
        long upper = password.chars().filter(Character::isUpperCase).count();
        long lower = password.chars().filter(Character::isLowerCase).count();
        long digit = password.chars().filter(Character::isDigit).count();
        long special = password.chars().filter(c -> !Character.isLetterOrDigit(c)).count();

        if (policy.getMinUpper() != null && upper < policy.getMinUpper()) {
            errors.put("uppercase", "Min uppercase required: " + policy.getMinUpper());
        }

        if (policy.getMinLower() != null && lower < policy.getMinLower()) {
            errors.put("lowercase", "Min lowercase required: " + policy.getMinLower());
        }

        if (policy.getMinNumber() != null && digit < policy.getMinNumber()) {
            errors.put("number", "Min numbers required: " + policy.getMinNumber());
        }

        if (policy.getMinSpecial() != null && special < policy.getMinSpecial()) {
            errors.put("special", "Min special chars required: " + policy.getMinSpecial());
        }

        // 🔥 USER DATA VALIDATION
        if ("N".equalsIgnoreCase(policy.getUsernameAllowed()) && user != null) {
        if (password.toLowerCase().contains(emp.getEmployeeName().toLowerCase())) {
        errors.put("username", "Password cannot contain username");
        }
        }

        if ("N".equalsIgnoreCase(policy.getEmailAllowed()) && user != null) {
        if (password.toLowerCase().contains(emp.getEmail().toLowerCase())) {
        errors.put("email", "Password cannot contain email");
        }
        }

        if ("N".equalsIgnoreCase(policy.getMobileAllowed()) && user != null) {
        if (password.contains(emp.getPhone())) {
        errors.put("mobile", "Password cannot contain mobile number");
        }
        }

        // if ("N".equalsIgnoreCase(policy.getDobAllowed()) && user != null) {
        // if (password.contains(emp.getDob())) {
        // errors.put("dob", "Password cannot contain DOB");
        // }
        // }

        //🔥 FINAL THROW
        if (!errors.isEmpty()) {
            throw new PasswordPolicyException(errors);
        }

        return "Password is valid";
    }
}