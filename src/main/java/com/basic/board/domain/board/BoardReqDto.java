package com.basic.board.domain.board;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

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
}
