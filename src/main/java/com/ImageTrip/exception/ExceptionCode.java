package com.ImageTrip.exception;

import lombok.Getter;

public enum ExceptionCode {

    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    LOGOUT(401, "Logout"),
    NO_COOKIE(403, "No Cookie"),
    INVALID_REFRESH_TOKEN_STATE(403, "Invalid Refresh Token State"),
    INVALID_ACCESS_TOKEN_STATE(403, "Invalid Access Token State"),
    ACCESS_TOKEN_EXPIRED(401, "Access Token Expired"),
    REFRESH_TOKEN_EXPIRED(401, "Refresh Token Expired"),
    UNMATCHED_WRITER(403, "글을 작성한 회원이 아닙니다."),
    SCHEDULE_NOT_FOUND(404, "존재하지 않는 일정입니다."),
    ALREADY_LIKED(409, "이미 좋아요 한 게시물입니다."),
    LIKE_NOT_FOUND(404, "좋아요 한 게시물이 아닙니다.");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}