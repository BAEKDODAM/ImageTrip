package com.ImageTrip.member.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class UpdatePasswordDto {

    private String currentPassword;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[!@#$%^&*(),.?\\\":{}|<>])(.{8,})$",
            message = "비밀번호는 영문, 특수문자 포함 8자리 이상이어야 합니다.")
    private String newPassword;
}
