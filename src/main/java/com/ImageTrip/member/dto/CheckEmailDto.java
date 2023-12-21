package com.ImageTrip.member.dto;

import lombok.Getter;

import javax.validation.constraints.Email;

@Getter
public class CheckEmailDto {
    @Email
    private String email;
}
