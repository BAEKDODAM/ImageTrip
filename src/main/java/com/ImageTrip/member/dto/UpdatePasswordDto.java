package com.ImageTrip.member.dto;

import lombok.Getter;

@Getter
public class UpdatePasswordDto {
    private String currentPassword;
    private String newPassword;
}
