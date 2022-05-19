package com.basic.board.domain.board;

import lombok.Getter;
import lombok.Setter;
import reactor.util.annotation.Nullable;

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

    @Getter
    @Setter
    public static class SearchBoard {

        // 1: 제목, 2: 내용, 3: 제목 + 내용
        @Pattern(regexp = "^(1|2|3)", message = "잘못된 검색 타입입니다.")
        @Nullable
        private String type;

        private String keyword;

        @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "잘못된 날짜 형식입니다.")
        @Nullable
        private String startDate;

        @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "잘못된 날짜 형식입니다.")
        @Nullable
        private String endDate;
    }
}
