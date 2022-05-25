package com.basic.board.domain.board.repository.impl;

import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.BoardReqDto;
import com.basic.board.domain.board.BoardResDto;
import com.basic.board.domain.board.entity.QBoard;
import com.basic.board.domain.board.entity.QComment;
import com.basic.board.domain.board.entity.QLiked;
import com.basic.board.domain.board.repository.custom.BoardRepositoryCustom;
import com.basic.board.domain.member.entity.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
    QBoard board = QBoard.board;
    QComment comment = new QComment("comment");
    QComment subComment = new QComment("subComment");
    QLiked liked = QLiked.liked;

    @Override
    public PageImpl<BoardResDto.BoardForList> getBoardList(BoardReqDto.SearchBoard searchBoard, PageRequest pageRequest) {
        BooleanBuilder builder = new BooleanBuilder();
        //검색 조건이 있는 경우
        if (searchBoard.getType() != null) {
            //제목
            if (searchBoard.getType().equals("1")) {
                builder.and(board.title.contains(searchBoard.getKeyword()));
            }
            //내용
            else if (searchBoard.getType().equals("2")) {
                builder.and(board.contents.contains(searchBoard.getKeyword()));
            }
            //제목 + 내용
            else {
                builder.and(board.title.contains(searchBoard.getKeyword())
                                .or(board.contents.contains(searchBoard.getKeyword())));
            }
        }

        //convert startDate, endDate
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (searchBoard.getStartDate() != null) {
            LocalDateTime startDate = LocalDate.parse(searchBoard.getStartDate(), dateTimeFormatter).atStartOfDay();
            builder.and(board.createAt.after(startDate));
        }
        if (searchBoard.getEndDate() != null) {
            LocalDateTime endDate = LocalDate.parse(searchBoard.getEndDate(), dateTimeFormatter).atTime(23, 59, 59);
            builder.and(board.createAt.before(endDate));
        }

        Pageable pageable = pageRequest.of();

        //countQuery
        JPAQuery<Long> countQuery = queryFactory.from(board).select(board.idx.count());
        //resultQuery
        JPAQuery<BoardResDto.BoardForList> resultQuery = queryFactory.from(board)
                .select(Projections.constructor(BoardResDto.BoardForList.class,
                        board.idx,
                        board.title,
                        board.views,
                        board.writer.nickname,
                        board.createAt));

        //count
        Long count = countQuery
                .where(builder)
                .fetchFirst();
        //result
        List<BoardResDto.BoardForList> results = resultQuery
                .where(builder)
                .fetch();

        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public BoardResDto.BoardDetail getBoardDetail(Long boardIdx) {
        return null;
    }
}
