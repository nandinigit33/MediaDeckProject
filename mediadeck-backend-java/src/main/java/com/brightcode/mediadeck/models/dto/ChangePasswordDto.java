package com.brightcode.mediadeck.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 5, max = 45, message = "Password must be between 5 and 45 characters")
    private String newPassword;

    @NotBlank
    private String verifyNewPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyNewPassword() {
        return verifyNewPassword;
    }

    public void setVerifyNewPassword(String verifyNewPassword) {
        this.verifyNewPassword = verifyNewPassword;
    }
}
