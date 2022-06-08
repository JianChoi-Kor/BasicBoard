package com.basic.board.domain.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MainResDto {

    @AllArgsConstructor
    @Getter
    public static class MemberInfo {
        private Long memberIdx;
        private String memberName;
    }
}
