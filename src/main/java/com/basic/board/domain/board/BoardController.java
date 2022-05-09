package com.basic.board.domain.board;

import com.basic.board.advice.Response;
import com.basic.board.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/board")
@RestController
public class BoardController {

    private final Response response;
    private final BoardService boardService;

    @PostMapping("")
    public ResponseEntity<?> insertBoard(@Validated BoardReqDto.InsertBoard input, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.insertBoard(input);
    }

    @GetMapping("")
    public ResponseEntity<?> boardList(PageRequest pageRequest) {
        PageImpl<BoardResDto.BoardForList> boardList = boardService.boardList(pageRequest);
        return response.success(boardList);
    }

    @GetMapping("/{boardIdx}")
    public ResponseEntity<?> boardDetail(@PathVariable Long boardIdx) {
        BoardResDto.BoardDetail boardForDetail = boardService.boardDetail(boardIdx);
        return response.success(boardForDetail);
    }

    @PatchMapping("/{boardIdx}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardIdx,
                                         @Validated BoardReqDto.UpdateBoard input, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.updateBoard(boardIdx, input);
    }

    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardIdx) {
        return boardService.deleteBoard(boardIdx);
    }


    @PostMapping("/comment/{boardIdx}")
    public ResponseEntity<?> insertComment(@Validated BoardReqDto.InsertComment insertComment, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.insertComment(insertComment);
    }

    @PatchMapping("/comment/{commentIdx}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentIdx,
                                           @Validated BoardReqDto.UpdateComment updateComment, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.updateComment(commentIdx, updateComment);
    }

    @DeleteMapping("/comment/{commentIdx}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentIdx) {
        return boardService.deleteComment(commentIdx);
    }
}
