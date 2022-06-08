package com.basic.board.domain.board.repository.impl;

import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.BoardReqDto;
import com.basic.board.domain.board.BoardResDto;
import com.basic.board.domain.board.entity.QBoard;
import com.basic.board.domain.board.entity.QComment;
import com.basic.board.domain.board.entity.QLiked;
import com.basic.board.domain.board.repository.custom.BoardRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QBoard board = QBoard.board;
    QComment comment = new QComment("comment");
    QComment subComment = new QComment("subComment");
    QLiked liked = new QLiked("liked");
    QLiked subLiked = new QLiked("subLiked");

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
    public PageImpl<BoardResDto.BoardForList> getBoardList(PageRequest pageRequest) {
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
                .fetchFirst();
        //result
        List<BoardResDto.BoardForList> results = resultQuery
                .fetch();

        return new PageImpl<>(results, pageable, count);
    }

    @Override
    public BoardResDto.BoardDetail getBoardDetail(Long boardIdx) {
        //board
        BoardResDto.BoardDetail boardDetail =
                queryFactory.select(Projections.constructor(BoardResDto.BoardDetail.class,
                                board.idx,
                                board.title,
                                board.contents,
                                board.views,
                                board.writer.idx,
                                board.writer.nickname,
                                board.createAt))
                        .from(board)
                        .where(board.idx.eq(boardIdx))
                        .fetchFirst();

        //comment
        List<BoardResDto.CommentForBoardDetail> commentForBoardDetailList =
                queryFactory.select(Projections.constructor(BoardResDto.CommentForBoardDetail.class,
                        comment.idx,
                        comment.contents,
                        comment.writer.nickname,
                        comment.createAt,
                        liked.idx.count()))
                        .from(comment)
                        .join(board).on(comment.boardIdx.eq(board.idx))
                        .leftJoin(liked).on(comment.idx.eq(liked.commentIdx))
                        .where(board.idx.eq(boardIdx).and(comment.parentCommentIdx.isNull()))
                        .groupBy(comment.idx)
                        .orderBy(comment.createAt.asc())
                        .fetch();

        //subComment
        List<BoardResDto.SubCommentForBoardDetail> subCommentForBoardDetailList =
                queryFactory.select(Projections.constructor(BoardResDto.SubCommentForBoardDetail.class,
                        subComment.idx,
                        subComment.parentCommentIdx,
                        subComment.contents,
                        subComment.writer.nickname,
                        subComment.createAt,
                        subLiked.idx.count()))
                        .from(subComment)
                        .join(comment).on(subComment.parentCommentIdx.eq(comment.idx))
                        .join(board).on(comment.boardIdx.eq(board.idx))
                        .leftJoin(subLiked).on(subComment.idx.eq(subLiked.commentIdx))
                        .where(board.idx.eq(boardIdx).and(subComment.parentCommentIdx.isNotNull()))
                        .groupBy(subComment.idx)
                        .orderBy(subComment.createAt.asc())
                        .fetch();

        //각각의 댓글에 대댓글 넣는 작업
        for (BoardResDto.CommentForBoardDetail commentForBoardDetail : commentForBoardDetailList) {
            List<BoardResDto.SubCommentForBoardDetail> eachSubCommentForBoardDetailList = new ArrayList<>();
            //대댓글 분류 작업
            for (BoardResDto.SubCommentForBoardDetail subCommentForBoardDetail : subCommentForBoardDetailList) {
                if (subCommentForBoardDetail.getParentCommentIdx().equals(commentForBoardDetail.getIdx())) {
                    eachSubCommentForBoardDetailList.add(subCommentForBoardDetail);
                }
            }
            commentForBoardDetail.setSubCommentForBoardDetailList(eachSubCommentForBoardDetailList);
        }
        //게시글에 댓글 리스트 추가
        boardDetail.setCommentForBoardDetailList(commentForBoardDetailList);
        return boardDetail;
    }

    @Transactional
    @Override
    public void updateBoardViews(Long boardIdx) {
        queryFactory.update(board)
                .where(board.idx.eq(boardIdx))
                .set(board.views, board.views.add(1))
                .execute();
    }
}
