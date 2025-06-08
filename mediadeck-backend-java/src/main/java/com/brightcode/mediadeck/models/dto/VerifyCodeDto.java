package com.brightcode.mediadeck.models.dto;

import jakarta.validation.constraints.NotBlank;

public class VerifyCodeDto {

    @NotBlank
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

