package com.basic.board.domain.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


public class AuthReqDto {

    @Getter
    @Setter
    public static class SignUp {

        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        private String email;

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        @NotBlank(message = "비밀번호 확인은 필수 입력값입니다.")
        private String passwordConfirm;

        @Pattern(regexp = "^[가-힣]*$", message = "이름은 한글만 입력해주세요.")
        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;
    }

    @Getter
    @Setter
    public static class Login {

        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;

        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
    }

    @Getter
    @Setter
    public static class Reissue {

        @NotBlank(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }

    @Getter
    @Setter
    public static class Logout {

        @NotBlank(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }
}
