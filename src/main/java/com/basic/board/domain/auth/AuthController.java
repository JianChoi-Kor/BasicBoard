package com.basic.board.domain.auth;

import com.basic.board.advice.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthController {

    private final Response response;
    private final AuthService authService;

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Validated AuthReqDto.SignUp signUp, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.signUp(signUp);
    }

    @GetMapping("/signin")
    public String signIn() {
        return "page/sign-in";
    }

    @ResponseBody
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Validated AuthReqDto.Login login, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.signIn(login);
    }

    @ResponseBody
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Validated AuthReqDto.Reissue reissue, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.reissue(reissue);
    }

    @ResponseBody
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authService.logout(request);
    }

    @RequestMapping("/fail")
    public String loginFail(HttpServletRequest request) {
        String msg = (String) request.getAttribute("msg");
        return "error/401";
    }
}
