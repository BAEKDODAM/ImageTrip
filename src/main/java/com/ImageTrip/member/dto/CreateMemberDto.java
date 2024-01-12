package com.ImageTrip.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@AllArgsConstructor
@Getter
public class CreateMemberDto {
    public CreateMemberDto(){}

    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;
}
