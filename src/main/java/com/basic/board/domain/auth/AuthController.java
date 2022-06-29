package com.basic.board.domain.auth;

import com.basic.board.advice.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"인증"})
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final Response response;
    private final AuthService authService;

    @ApiOperation(value = "회원가입", notes = "회원가입 기능")
    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Validated AuthReqDto.SignUp signUp, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.signUp(signUp);
    }

    @ApiOperation(value = "로그인", notes = "로그인 기능")
    @ResponseBody
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody @Validated AuthReqDto.Login login) {
        return authService.signIn(login);
    }

    @ApiOperation(value = "토큰 갱신", notes = "토큰 갱신 기능", authorizations = @Authorization(value = "Bearer"))
    @ResponseBody
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Validated AuthReqDto.Reissue reissue, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return authService.reissue(reissue);
    }

    @ApiOperation(value = "로그아웃", notes = "로그아웃 기능", authorizations = @Authorization(value = "Bearer"))
    @ResponseBody
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authService.logout(request);
    }
}
