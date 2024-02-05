package com.ImageTrip.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@Getter
public class CreateMemberDto {
    public CreateMemberDto(){}

    @Email
    private String email;

    @NotBlank
    private String name;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[!@#$%^&*(),.?\\\":{}|<>])(.{8,})$",
    message = "비밀번호는 영문, 특수문자 포함 8자리 이상이어야 합니다.")
    private String password;
}
