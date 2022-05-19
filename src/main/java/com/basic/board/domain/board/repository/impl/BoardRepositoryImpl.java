package com.basic.board.domain.board.repository.impl;

import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.BoardReqDto;
import com.basic.board.domain.board.BoardResDto;
import com.basic.board.domain.board.entity.QBoard;
import com.basic.board.domain.board.entity.QComment;
import com.basic.board.domain.board.repository.custom.BoardRepositoryCustom;
import com.basic.board.domain.member.entity.QMember;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    QMember member = QMember.member;
    QBoard board = QBoard.board;
    QComment comment = QComment.comment;

    @Override
    public PageImpl<BoardResDto.BoardForList> getBoardList(BoardReqDto.SearchBoard searchBoard, PageRequest pageRequest) {

        //convert startDate, endDate
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(searchBoard.getStartDate(), dateTimeFormatter).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(searchBoard.getEndDate(), dateTimeFormatter).atTime(23, 59, 59);



        return null;
    }
}
