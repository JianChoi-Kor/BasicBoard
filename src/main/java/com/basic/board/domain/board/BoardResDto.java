package com.basic.board.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BoardResDto {


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BoardForList {

        private Long idx;
        private String title;
        private Long views;
        private String writerName;
        private LocalDateTime createAt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BoardForDetail {

        private Long idx;
        private String title;
        private String contents;
        private Long views;
        private String writerName;
        private LocalDateTime createAt;
    }
}
