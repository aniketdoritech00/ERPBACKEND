package com.doritech.EmployeeService.response;

public class PasswordPolicyResponse {

    private Integer id;
    private String name;
    private Integer minLength;
    private Integer maxLength;
    private String status;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getMinLength() {
        return minLength;
    }
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }
    public Integer getMaxLength() {
        return maxLength;
    }
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public PasswordPolicyResponse(Integer id, String name, Integer minLength, Integer maxLength, String status) {
        this.id = id;
        this.name = name;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.status = status;
    }

    public PasswordPolicyResponse() {
        super();
    }

    
}
