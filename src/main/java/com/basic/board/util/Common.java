package com.basic.board.util;

import com.basic.board.domain.auth.security.SecurityUtil;
import com.basic.board.domain.member.entity.Member;
import com.basic.board.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Common {

    private final MemberRepository memberRepository;

    public Member getMember() {
        //SecurityContext 에 담겨 있는 authentication 정보
        Authentication authentication = SecurityUtil.getCurrentAuthentication();

        Member member = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("No authentication information."));
        return member;
    }

    public Member isMember() {
        //SecurityContext 에 담겨 있는 authentication 정보
        Authentication authentication = SecurityUtil.getCurrentAuthentication();

        Member member = memberRepository.findByEmail(authentication.getName())
                .orElse(null);
        return member;
    }
}
