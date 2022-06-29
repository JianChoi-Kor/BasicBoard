package com.basic.board.domain.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class AuthReqDto {

    @Getter
    @Setter
    public static class SignUp {

        @ApiModelProperty(required = true, value = "이메일", example = "test@test.com")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        private String email;

        @ApiModelProperty(required = true, value = "비밀번호")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        @ApiModelProperty(required = true, value = "비밀번호 확인")
        @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
        private String passwordConfirm;

        @ApiModelProperty(required = true, value = "닉네임", example = "마동석")
        @Pattern(regexp = "^[가-힣]*$", message = "닉네임은 한글만 입력해주세요.")
        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        private String nickname;
    }

    @Getter
    @Setter
    public static class Login {

        @ApiModelProperty(required = true, value = "이메일", example = "test@test.com")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        @NotBlank(message = "{common.email.notblank}")
        private String email;
        @ApiModelProperty(required = true, value = "비밀번호")
        @NotBlank(message = "{common.password.notblank}")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @Setter
    public static class Reissue {

        @ApiModelProperty(required = true, value = "리프레시 토큰", example = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NTUxMDkzNjN9.lzku4kCr_ppT9x-8U_YYXK729r4o5BT8nYUFYTAecN8")
        @NotBlank(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }
}
