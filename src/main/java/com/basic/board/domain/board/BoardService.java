package com.basic.board.domain.board;

import com.basic.board.domain.PageRequest;
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
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikedRepository likedRepository;

    public ResponseEntity<?> insertBoard(BoardReqDto.InsertBoard input) {
        Member member = common.getMember();




        return null;
    }

    public PageImpl<BoardResDto.BoardForList> boardList(PageRequest pageRequest) {
        return null;
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
        return null;
    }

    public ResponseEntity<?> updateComment(Long commentIdx, BoardReqDto.UpdateComment input) {
        return null;
    }

    public ResponseEntity<?> deleteComment(Long commentIdx) {
        return null;
    }
}
