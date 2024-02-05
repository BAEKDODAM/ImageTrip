package com.ImageTrip.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;

public class MemberDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckEmailDto{
        private String email;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckNameDto {
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckPasswordDto{
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateMemberDto {

        private String email;
        private String name;

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[!@#$%^&*(),.?\\\":{}|<>])(.{8,})$",
                message = "비밀번호는 영문, 특수문자 포함 8자리 이상이어야 합니다.")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetAccountResponseDto{
        private String email;
        private String name;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateNameDto{
        private String newName;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePasswordDto{
        private String currentPassword;
        private String newPassword;
    }

}
