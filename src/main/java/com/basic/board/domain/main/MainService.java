package com.basic.board.domain.main;

import com.basic.board.advice.Response;
import com.basic.board.domain.member.entity.Member;
import com.basic.board.util.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MainService {

    private final Response response;
    private final Common common;

    public ResponseEntity<?> authInfo() {
        Member member = common.getMember();
        return response.success(new MainResDto.MemberInfo(member.getIdx(), member.getNickname()));
    }
}
