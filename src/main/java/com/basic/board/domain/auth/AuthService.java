package com.basic.board.domain.auth;

import com.basic.board.advice.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final Response response;

    public ResponseEntity<?> signUp(AuthReqDto.SignUp signUp) {
        return null;
    }

    public ResponseEntity<?> login(AuthReqDto.Login login) {
        return null;
    }

    public ResponseEntity<?> reissue(AuthReqDto.Reissue reissue) {
        return null;
    }

    public ResponseEntity<?> logout(AuthReqDto.Logout logout) {
        return null;
    }
}
