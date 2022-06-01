package com.basic.board.domain.auth;

import com.basic.board.advice.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final Response response;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Validated AuthReqDto.SignUp signUp, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.signUp(signUp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated AuthReqDto.Login login, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.login(login);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Validated AuthReqDto.Reissue reissue, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.reissue(reissue);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

    @RequestMapping("/fail")
    public String loginFail(HttpServletRequest request) {
        String msg = (String) request.getAttribute("msg");
        return "page/fail";
    }
}
