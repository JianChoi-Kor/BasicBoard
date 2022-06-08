package com.basic.board.domain.board;

import com.basic.board.advice.Response;
import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.entity.Board;
import com.basic.board.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/board")
@Controller
public class BoardController {

    private final Response response;
    private final BoardService boardService;

    //insert, update page
    @GetMapping("/write")
    public String insertBoard() {
        return boardService.insertBoard();
    }

    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<?> insertBoard(@RequestBody @Validated BoardReqDto.InsertBoard input, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.insertBoard(input);
    }

    @GetMapping("/main")
    public String boardList(@Validated BoardReqDto.SearchBoard search, Errors searchErrors,
                                       PageRequest pageRequest, Model model) {
        //valid check
        if (searchErrors.hasErrors()) {
            //first error
            FieldError fieldError = searchErrors.getFieldErrors().get(0);
            Helper.errorMsg(fieldError.getField(), fieldError.getDefaultMessage());
            return null;
        }

        //type && keyword check
        if ((search.getKeyword() != null && !search.getKeyword().equals("")) && search.getType() == null) {
            Helper.errorMsg("잘못된 검색 조건입니다.");
            return null;
        }
        if (search.getType() != null && search.getKeyword() == null) {
            Helper.errorMsg("잘못된 검색 조건입니다.");
            return null;
        }
        List<String> typeList = Arrays.asList("title", "contents", "titleAndContents");
        if (search.getType() !=  null && !typeList.contains(search.getType())) {
            Helper.errorMsg("잘못된 검색 조건입니다.");
            return null;
        }

        //startDate, endDate check
        if (search.getStartDate() != null && search.getEndDate() != null) {
            //convert startDate, endDate
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime startDate = LocalDate.parse(search.getStartDate(), dateTimeFormatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(search.getEndDate(), dateTimeFormatter).atTime(23, 59, 59);
            if (startDate.isAfter(endDate)) {
                Helper.errorMsg("잘못된 검색 조건입니다.");
                return null;
            }
        }

        PageImpl<BoardResDto.BoardForList> boardList = boardService.boardList(search, pageRequest);
        model.addAttribute(boardList);

        return "page/board-main";
    }

    @GetMapping("/{boardIdx}")
    public String boardDetail(@PathVariable Long boardIdx, Model model) {
        BoardResDto.BoardDetail boardDetail = boardService.boardDetail(boardIdx);
        if (boardDetail == null) {
            Helper.errorMsg("잘못된 요청입니다.");
            return null;
        }
        model.addAttribute(boardDetail);

        return "page/boardDetail";
    }

    @GetMapping("/update/{boardIdx}")
    public String updateBoard(@PathVariable Long boardIdx, Model model) {
        Board board = boardService.updateBoard(boardIdx);
        if (board == null) {
            Helper.errorMsg("잘못된 요청입니다.");
            return null;
        }
        model.addAttribute(board);

        return "page/boardWrite";
    }

    @PatchMapping("/update/{boardIdx}")
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


    @PostMapping("/comment")
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
