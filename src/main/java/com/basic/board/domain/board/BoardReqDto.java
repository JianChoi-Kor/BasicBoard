package com.basic.board.domain.board;

import io.swagger.annotations.ApiModelProperty;
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
    public static class InsertAndUpdate {

        @ApiModelProperty(required = true, value = "제목", example = "제목입니다.")
        @NotBlank(message = "제목은 필수 입력값입니다.")
        private String title;

        @ApiModelProperty(required = true, value = "내용", example = "내용입니다.")
        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String contents;
    }

    @Getter
    @Setter
    public static class InsertComment {

        @ApiModelProperty(required = true, value = "게시글 고유값", example = "1")
        @NotNull(message = "게시글 고유값은 필수 입력값입니다.")
        @Positive(message = "잘못된 게시글 고유값입니다.")
        private Long boardIdx;

        @ApiModelProperty(value = "상위 댓글 고유값", example = "1")
        private Long commentIdx;

        @ApiModelProperty(required = true, value = "댓글 내용", example = "댓글입니다.")
        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String contents;
    }

    @Getter
    @Setter
    public static class UpdateComment {

        @ApiModelProperty(required = true, value = "댓글 내용", example = "댓글입니다.")
        @NotBlank(message = "내용은 필수 입력값입니다.")
        private String contents;
    }

    @Getter
    @Setter
    public static class SearchBoard {

        @ApiModelProperty(value = "검색 타입 (1: 제목, 2: 내용, 3: 제목 + 내용)", example = "1")
        @Pattern(regexp = "^(1|2|3)", message = "잘못된 검색 타입입니다.")
        @Nullable
        private String type;

        @ApiModelProperty(value = "검색어", example = "어제 뉴스")
        private String keyword;

        @ApiModelProperty(value = "검색 시작 날짜(yyyy-MM-dd)", example = "2021-01-01")
        @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "잘못된 날짜 형식입니다.")
        @Nullable
        private String startDate;

        @ApiModelProperty(value = "검색 종료 날짜(yyyy-MM-dd)", example = "2022-12-31")
        @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[0-1])$", message = "잘못된 날짜 형식입니다.")
        @Nullable
        private String endDate;
    }
}
