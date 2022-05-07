package com.basic.board.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class AuthResDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class TokenInfo {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Long refreshTokenExpirationTime;
    }
}
