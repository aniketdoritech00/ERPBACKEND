package com.doritech.EmployeeService.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "PASSWORD_POLICY")
public class PasswordPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PASSWORD_POLICY_ID")
    private Integer id;

    @Column(name = "PASSWORD_POLICY_NAME", nullable = false)
    private String name;

    @Column(name = "MIN_NO_OF_U_ALPHA")
    private Integer minUpper;

    @Column(name = "MIN_NO_OF_L_ALPHA")
    private Integer minLower;

    @Column(name = "MIN_NO_OF_NUM")
    private Integer minNumber;

    @Column(name = "MIN_NO_OF_SP_CHAR")
    private Integer minSpecial;

    @Column(name = "MIN_PASS_LEN")
    private Integer minLength;

    @Column(name = "MAX_PASS_LEN")
    private Integer maxLength;

    @Column(name = "CHANGE_AT_FIRST_LOGIN")
    private String changeAtFirstLogin;

    @Column(name = "NO_OF_DAYS_TO_EXPIRE")
    private Integer expiryDays;

    @Column(name = "NO_OF_WRONG_ATTEMPT_LOCK_PASS")
    private Integer maxWrongAttempts;

    @Column(name = "HOURS_TO_UNLOCK_PASS")
    private Integer unlockHours;

    @Column(name = "LAST_NO_OF_PASS_TO_CHECK")
    private Integer lastPasswordsCheck;

    @Column(name = "NO_OF_CHAR_TO_CHECK_LAST_PASS")
    private Integer lastCharCheck;

    @Column(name = "ENCRYPTION_METHOD")
    private String encryptionMethod;

    @Column(name = "USER_NAME_ALLOWED")
    private String usernameAllowed;

    @Column(name = "USER_ID_ALLOWED")
    private String userIdAllowed;

    @Column(name = "USER_EMAIL_ID_ALLOWED")
    private String emailAllowed;

    @Column(name = "USER_MOBILE_ALLOWED")
    private String mobileAllowed;

    @Column(name = "USER_DOB_ALLOWED")
    private String dobAllowed;

    @Column(name = "NO_OF_DAYS_EXPIRE_MSG")
    private Integer expiryMsgDays;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @PrePersist
	void prePersist() {
		createdOn = LocalDateTime.now();
	}

	@PreUpdate
	void preUpdate() {
		modifiedOn = LocalDateTime.now();
	}

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    
}