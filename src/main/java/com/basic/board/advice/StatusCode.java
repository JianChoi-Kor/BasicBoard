package com.basic.board.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum StatusCode {

    SUCCESS(200, "0000", "Success");

    private int status;
    private final String code;
    private final String message;

    StatusCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
