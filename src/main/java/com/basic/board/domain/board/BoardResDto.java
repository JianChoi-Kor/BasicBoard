package com.basic.board.domain.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NoArgsConstructor
    @Getter
    @Setter
    public static class BoardDetail {

        private Long idx;
        private String title;
        private String contents;
        private Long views;
        private String writerName;
        private LocalDateTime createAt;
        private List<CommentForBoardDetail> commentForBoardDetailList;

        public BoardDetail(Long idx, String title, String contents, Long views, String writerName, LocalDateTime createAt) {
            this.idx = idx;
            this.title = title;
            this.contents = contents;
            this.views = views;
            this.writerName = writerName;
            this.createAt = createAt;
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class CommentForBoardDetail {
        private Long idx;
        private String contents;
        private String writerName;
        private LocalDateTime createAt;
        private Long likeCount;
        private List<SubCommentForBoardDetail> subCommentForBoardDetailList;

        public CommentForBoardDetail(Long idx, String contents, String writerName, LocalDateTime createAt, Long likeCount) {
            this.idx = idx;
            this.contents = contents;
            this.writerName = writerName;
            this.createAt = createAt;
            this.likeCount = likeCount;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class SubCommentForBoardDetail {
        private Long idx;
        private Long parentCommentIdx;
        private String contents;
        private String writerName;
        private LocalDateTime createAt;
        private Long likeCount;
    }
}
