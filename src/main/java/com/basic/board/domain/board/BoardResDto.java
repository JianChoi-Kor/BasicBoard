package com.basic.board.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    public static class BoardDetail {

        private Long idx;
        private String title;
        private String contents;
        private Long views;
        private String writerName;
        private LocalDateTime createAt;
        private List<CommentForBoardDetail> commentForBoardDetailList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CommentForBoardDetail {
        private Long idx;
        private String contents;
        private String writerName;
        private LocalDateTime createAt;
        private Long likeCount;
        private List<SubCommentForBoardDetail> subCommentForBoardDetailList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SubCommentForBoardDetail {
        private Long idx;
        private String contents;
        private String writerName;
        private LocalDateTime createAt;
        private Long likeCount;
    }
}
