package com.basic.board.domain.board;

import com.basic.board.advice.Response;
import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.entity.Board;
import com.basic.board.domain.board.entity.Comment;
import com.basic.board.domain.board.repository.BoardRepository;
import com.basic.board.domain.board.repository.CommentRepository;
import com.basic.board.domain.board.repository.LikedRepository;
import com.basic.board.domain.member.entity.Member;
import com.basic.board.util.Common;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final Common common;
    private final Response response;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikedRepository likedRepository;

    public ResponseEntity<?> insertBoard(BoardReqDto.InsertBoard input) {
        Member member = common.getMember();

        Board board = Board.builder()
                .title(input.getTitle())
                .contents(input.getContents())
                .views(0L)
                .writer(member)
                .deleteYn(false)
                .build();
        boardRepository.save(board);
        return response.success("게시글이 등록되었습니다.");
    }

    public PageImpl<BoardResDto.BoardForList> boardList(BoardReqDto.SearchBoard searchBoard, PageRequest pageRequest) {
        return boardRepository.getBoardList(searchBoard, pageRequest);
    }

    public BoardResDto.BoardDetail boardDetail(Long boardIdx) {
        return null;
    }

    public ResponseEntity<?> updateBoard(Long boardIdx, BoardReqDto.UpdateBoard input) {
        return null;
    }

    public ResponseEntity<?> deleteBoard(Long boardIdx) {
        return null;
    }

    public ResponseEntity<?> insertComment(BoardReqDto.InsertComment input) {
        Member member = common.getMember();

        //상위 댓글
        Comment parentComment = commentRepository.findByIdx(input.getCommentIdx());
        if (parentComment == null) {
            return response.fail("잘못된 요청입니다.");
        }
        if (parentComment.getParentCommentIdx() != null) {
            return response.fail("잘못된 요청입니다.");
        }
        if (parentComment.isDeleteYn()) {
            return response.fail("삭제된 댓글에는 댓글을 달 수 없습니다.");
        }

        //등록하는 댓글
        Comment comment = Comment.builder()
                .boardIdx(input.getBoardIdx())
                .parentCommentIdx(input.getCommentIdx() != null ? input.getCommentIdx() : null)
                .contents(input.getContents())
                .writer(member)
                .deleteYn(false)
                .build();
        commentRepository.save(comment);
        return response.success("댓글이 등록되었습니다.");
    }

    public ResponseEntity<?> updateComment(Long commentIdx, BoardReqDto.UpdateComment input) {
        return null;
    }

    public ResponseEntity<?> deleteComment(Long commentIdx) {
        return null;
    }
}
