package com.doritech.EmployeeService.request;

public class PasswordPolicyRequest {

    private String name;

    private Integer minUpper;
    private Integer minLower;
    private Integer minNumber;
    private Integer minSpecial;

    private Integer minLength;
    private Integer maxLength;

    private String changeAtFirstLogin;

    private Integer expiryDays;
    private Integer maxWrongAttempts;
    private Integer unlockHours;

    private Integer lastPasswordsCheck;
    private Integer lastCharCheck;

    private String encryptionMethod;

    private String usernameAllowed;
    private String userIdAllowed;
    private String emailAllowed;
    private String mobileAllowed;
    private String dobAllowed;

    private Integer expiryMsgDays;

    private String status;
    

    public String getChangeAtFirstLogin() {
        return changeAtFirstLogin;
    }
    public void setChangeAtFirstLogin(String changeAtFirstLogin) {
        this.changeAtFirstLogin = changeAtFirstLogin;
    }
    public Integer getExpiryDays() {
        return expiryDays;
    }
    public void setExpiryDays(Integer expiryDays) {
        this.expiryDays = expiryDays;
    }
    public Integer getMaxWrongAttempts() {
        return maxWrongAttempts;
    }
    public void setMaxWrongAttempts(Integer maxWrongAttempts) {
        this.maxWrongAttempts = maxWrongAttempts;
    }
    public Integer getUnlockHours() {
        return unlockHours;
    }
    public void setUnlockHours(Integer unlockHours) {
        this.unlockHours = unlockHours;
    }
    public Integer getLastPasswordsCheck() {
        return lastPasswordsCheck;
    }
    public void setLastPasswordsCheck(Integer lastPasswordsCheck) {
        this.lastPasswordsCheck = lastPasswordsCheck;
    }
    public Integer getLastCharCheck() {
        return lastCharCheck;
    }
    public void setLastCharCheck(Integer lastCharCheck) {
        this.lastCharCheck = lastCharCheck;
    }
    public String getEncryptionMethod() {
        return encryptionMethod;
    }
    public void setEncryptionMethod(String encryptionMethod) {
        this.encryptionMethod = encryptionMethod;
    }
    public String getUsernameAllowed() {
        return usernameAllowed;
    }
    public void setUsernameAllowed(String usernameAllowed) {
        this.usernameAllowed = usernameAllowed;
    }
    public String getUserIdAllowed() {
        return userIdAllowed;
    }
    public void setUserIdAllowed(String userIdAllowed) {
        this.userIdAllowed = userIdAllowed;
    }
    public String getEmailAllowed() {
        return emailAllowed;
    }
    public void setEmailAllowed(String emailAllowed) {
        this.emailAllowed = emailAllowed;
    }
    public String getMobileAllowed() {
        return mobileAllowed;
    }
    public void setMobileAllowed(String mobileAllowed) {
        this.mobileAllowed = mobileAllowed;
    }
    public String getDobAllowed() {
        return dobAllowed;
    }
    public void setDobAllowed(String dobAllowed) {
        this.dobAllowed = dobAllowed;
    }
    public Integer getExpiryMsgDays() {
        return expiryMsgDays;
    }
    public void setExpiryMsgDays(Integer expiryMsgDays) {
        this.expiryMsgDays = expiryMsgDays;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getMinUpper() {
        return minUpper;
    }
    public void setMinUpper(Integer minUpper) {
        this.minUpper = minUpper;
    }
    public Integer getMinLower() {
        return minLower;
    }
    public void setMinLower(Integer minLower) {
        this.minLower = minLower;
    }
    public Integer getMinNumber() {
        return minNumber;
    }
    public void setMinNumber(Integer minNumber) {
        this.minNumber = minNumber;
    }
    public Integer getMinSpecial() {
        return minSpecial;
    }
    public void setMinSpecial(Integer minSpecial) {
        this.minSpecial = minSpecial;
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

    
}
