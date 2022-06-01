package com.basic.board.domain.auth;

import com.basic.board.advice.Response;
import com.basic.board.domain.auth.jwt.JwtTokenProvider;
import com.basic.board.domain.auth.security.SecurityUtil;
import com.basic.board.domain.member.entity.Member;
import com.basic.board.domain.member.enums.Authority;
import com.basic.board.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final Response response;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate redisTemplate;

    public ResponseEntity<?> signUp(AuthReqDto.SignUp signUp) {
        //이메일 중복 확인
        if (memberRepository.existsByEmail(signUp.getEmail())) {
            return response.fail("이미 회원가입된 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        //비밀번호 일치 여부 확인
        if (!signUp.getPassword().equals(signUp.getPasswordConfirm())) {
            return response.fail("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //Member 객체 생성 및 저장
        Member member = Member.builder()
                .email(signUp.getEmail())
                .password(passwordEncoder.encode(signUp.getPassword()))
                .nickname(signUp.getNickname())
                .roles(Collections.singletonList(Authority.ROLE_USER.name()))
                .build();
        memberRepository.save(member);

        return response.success("회원가입에 성공했습니다.");
    }

    public ResponseEntity<?> signIn(AuthReqDto.Login login) {

        //1. Login ID/PW 를 기반으로 Authentication 객체 생성
        //이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = login.toAuthentication();

        //2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        //authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //3. 인증 정보를 기반으로 JWT 토큰 생성
        AuthResDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        //4. RefreshToken Redis 저장 (expirationTime 설정을 통해 자동 삭제 처리)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "로그인에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> reissue(AuthReqDto.Reissue reissue) {
        //SecurityContext 에 담겨 있는 authentication 정보
        Authentication authentication = SecurityUtil.getCurrentAuthentication();

        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        //1. Refresh Token 검증
        if (!jwtTokenProvider.validateToken(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //2. Redis 에서 memberEmail 을 기반으로 저장된 Refresh Token 값을 가져옵니다.
        String refreshToken = (String) redisTemplate.opsForValue().get("RT:" + member.getEmail());
        //로그아웃되어 Redis 에 Refresh Token 이 존재하지 않는 경우 처리
        if (ObjectUtils.isEmpty(refreshToken)) {
            return response.fail("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);
        }
        if (!refreshToken.equals(reissue.getRefreshToken())) {
            return response.fail("Refresh Token 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        //3. 새로운 토큰 생성
        AuthResDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        //4. Refresh Token 업데이트
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), tokenInfo.getRefreshToken(), tokenInfo.getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS);

        return response.success(tokenInfo, "인증 정보가 갱신되었습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> logout(HttpServletRequest request) {
        //SecurityContext 에 담겨 있는 authentication 정보
        Authentication authentication = SecurityUtil.getCurrentAuthentication();

        //HttpHeaders 에 담겨 있는 AccessToken 정보
        String token = resolveToken(request);
        if (token == null) {
            return response.fail("잘못된 요청입니다.");
        }

        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));

        //1. Redis 에서 해당 memberEmail 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + authentication.getName()) != null) {
            //Refresh Token 삭제
            redisTemplate.delete("RT:" + authentication.getName());
        }

        //2. 해당 Access Token 유효시간을 가지고 와서 BlackList 로 저장하기
        Long expiration = jwtTokenProvider.getExpiration(token);
        redisTemplate.opsForValue()
                .set(token, "logout", expiration, TimeUnit.MILLISECONDS);

        return response.success("로그아웃 되었습니다.");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            //'Bearer ' 공백까지 총 7자리
            return bearerToken.substring(7);
        }
        return null;
    }
}
