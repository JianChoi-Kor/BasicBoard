package com.basic.board.domain.board;

import com.basic.board.advice.Response;
import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.entity.Board;
import com.basic.board.domain.board.entity.Comment;
import com.basic.board.domain.board.entity.Liked;
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

    private final Response response;
    private final Common common;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikedRepository likedRepository;

    public ResponseEntity<?> insertBoard(BoardReqDto.InsertAndUpdate input) {
        Member member = common.getMember();

        //board
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
        Member member = common.isMember();
        Long memberIdx = null;
        if (member != null) {
            memberIdx = member.getIdx();
        }

        boardRepository.updateBoardViews(boardIdx);
        return boardRepository.getBoardDetail(boardIdx, memberIdx);
    }

    public ResponseEntity<?> updateBoard(Long boardIdx, BoardReqDto.InsertAndUpdate input) {
        Member member = common.getMember();

        //board check
        Board board = boardRepository.findByIdx(boardIdx);
        if (board == null) {
            return response.fail("해당하는 게시글이 존재하지 않습니다.");
        }
        if (!board.getWriter().getIdx().equals(member.getIdx())) {
            return response.fail("잘못된 요청입니다.");
        }
        if (board.isDeleteYn()) {
            return response.fail("삭제된 게시글은 수정할 수 없습니다.");
        }

        //update board
        board.setTitle(input.getTitle());
        board.setContents(input.getContents());
        boardRepository.save(board);

        return response.success("게시글이 수정되었습니다.");
    }

    public ResponseEntity<?> deleteBoard(Long boardIdx) {
        Member member = common.getMember();

        //board check
        Board board = boardRepository.findByIdx(boardIdx);
        if (board == null) {
            return response.fail("해당하는 게시글이 존재하지 않습니다.");
        }
        if (!board.getWriter().getIdx().equals(member.getIdx())) {
            return response.fail("잘못된 요청입니다.");
        }
        if (board.isDeleteYn()) {
            return response.fail("잘못된 요청입니다.");
        }

        //delete board
        board.setDeleteYn(true);
        boardRepository.save(board);

        return response.success("게시글이 삭제되었습니다.");
    }

    public ResponseEntity<?> insertComment(BoardReqDto.InsertComment input) {
        Member member = common.getMember();

        //대댓글인 경우
        if (input.getCommentIdx() != null) {
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
        }
        //댓글인 경우
        else {
            //게시글
            Board board = boardRepository.findByIdx(input.getBoardIdx());
            if (board == null) {
                return response.fail("잘못된 요청입니다.");
            }
            if (board.isDeleteYn()) {
                return response.fail("삭제된 게시글에는 댓글을 달 수 없습니다.");
            }
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
        Member member = common.getMember();

        Comment comment = commentRepository.findByIdx(commentIdx);
        if (comment == null) {
            return response.fail("해당하는 댓글이 존재하지 않습니다.");
        }
        if (!comment.getWriter().getIdx().equals(member.getIdx())) {
            return response.fail("잘못된 요청입니다.");
        }
        if (comment.isDeleteYn()) {
            return response.fail("삭제된 댓글은 수정할 수 없습니다.");
        }

        //update comment
        comment.setContents(input.getContents());
        commentRepository.save(comment);

        return response.success("댓글이 수정되었습니다.");
    }

    public ResponseEntity<?> deleteComment(Long commentIdx) {
        Member member = common.getMember();

        Comment comment = commentRepository.findByIdx(commentIdx);
        if (comment == null) {
            return response.fail("해당하는 댓글이 존재하지 않습니다.");
        }
        if (!comment.getWriter().getIdx().equals(member.getIdx())) {
            return response.fail("잘못된 요청입니다.");
        }
        if (comment.isDeleteYn()) {
            return response.fail("잘못된 요청입니다.");
        }

        //delete comment
        comment.setDeleteYn(true);
        commentRepository.save(comment);

        return response.success("댓글이 삭제되었습니다.");
    }

    public ResponseEntity<?> likeComment(Long commentIdx) {
        Member member = common.getMember();

        Comment comment = commentRepository.findByIdx(commentIdx);
        if (comment == null) {
            return response.fail("해당하는 댓글이 존재하지 않습니다.");
        }
        Liked liked = likedRepository.findByCommentIdxAndMemberIdx(commentIdx, member.getIdx());
        //해당 댓글에 대한 로그인 회원의 좋아요가 없는 경우
        if (liked == null) {
            likedRepository.save(new Liked(commentIdx, member.getIdx()));
        }
        //해당 댓글에 대한 로그인 회원의 좋아요가 있는 경우 (좋아요 취소)
        else {
            likedRepository.delete(liked);
        }
        return response.success();
    }
}
