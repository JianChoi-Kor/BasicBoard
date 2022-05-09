package com.basic.board.domain.board;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class BoardReqDto {

    @Getter
    @Setter
    public static class InsertBoard {

        @NotBlank(message = "제목은 필수 입력값입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String contents;
    }

    @Getter
    @Setter
    public static class UpdateBoard {

        @NotBlank(message = "제목은 필수 입력값입니다.")
        private String title;

        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String contents;
    }

    @Getter
    @Setter
    public static class InsertComment {

        @NotNull(message = "게시판 고유값은 필수 입력값입니다.")
        @Positive(message = "잘못된 게시판 고유값입니다.")
        private Long boardIdx;

        private Long commentIdx;

        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String contents;
    }

    @Getter
    @Setter
    public static class UpdateComment {

        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String contents;
    }
}
