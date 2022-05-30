package com.basic.board.domain.board;

import com.basic.board.domain.PageRequest;
import com.basic.board.util.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/api/board")
@Controller
public class BoardController {

    private final BoardService boardService;

    //insert, update page
    @GetMapping("/write")
    public String insertBoard() {
        return "page/boardWrite";
    }

    @PostMapping("/write")
    public String insertBoard(@Validated BoardReqDto.InsertBoard input, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            //first error
            FieldError fieldError = errors.getFieldErrors().get(0);
            Helper.errorMsg(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return boardService.insertBoard(input);
    }

    @GetMapping("/list")
    public String boardList(@Validated BoardReqDto.SearchBoard search, Errors searchErrors,
                                       PageRequest pageRequest) {
        //valid check
        if (searchErrors.hasErrors()) {
            //first error
            FieldError fieldError = searchErrors.getFieldErrors().get(0);
            Helper.errorMsg(fieldError.getField(), fieldError.getDefaultMessage());
        }

        //type && keyword check
        if ((search.getKeyword() != null && !search.getKeyword().equals("")) && search.getType() == null) {
            Helper.errorMsg("잘못된 검색 조건입니다.");
        }
        if (search.getType() != null && search.getKeyword() == null) {
            Helper.errorMsg("잘못된 검색 조건입니다.");
        }
        List<String> typeList = Arrays.asList("title", "contents", "titleAndContents");
        if (search.getType() !=  null && !typeList.contains(search.getType())) {
            Helper.errorMsg("잘못된 검색 조건입니다.");
        }

        //startDate, endDate check
        if (search.getStartDate() != null && search.getEndDate() != null) {
            //convert startDate, endDate
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime startDate = LocalDate.parse(search.getStartDate(), dateTimeFormatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(search.getEndDate(), dateTimeFormatter).atTime(23, 59, 59);
            if (startDate.isAfter(endDate)) {
                Helper.errorMsg("잘못된 검색 조건입니다.");
            }
        }

        PageImpl<BoardResDto.BoardForList> boardList = boardService.boardList(search, pageRequest);
        return "page/boardMain";
    }

    @GetMapping("/{boardIdx}")
    public String boardDetail(@PathVariable Long boardIdx) {
        BoardResDto.BoardDetail boardForDetail = boardService.boardDetail(boardIdx);
        return "page/boardDetail";
    }

    @GetMapping("/update/{boardIdx}")
    public String updateBoard(@PathVariable Long boardIdx) {
        return boardService.updateBoard(boardIdx);
    }

    @PatchMapping("/update/{boardIdx}")
    public String updateBoard(@PathVariable Long boardIdx,
                                         @Validated BoardReqDto.UpdateBoard input, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            //first error
            FieldError fieldError = errors.getFieldErrors().get(0);
            Helper.errorMsg(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return boardService.updateBoard(boardIdx, input);
    }

    @DeleteMapping("/{boardIdx}")
    public String deleteBoard(@PathVariable Long boardIdx) {
        return boardService.deleteBoard(boardIdx);
    }


    @PostMapping("/comment")
    public String insertComment(@Validated BoardReqDto.InsertComment insertComment, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            //first error
            FieldError fieldError = errors.getFieldErrors().get(0);
            Helper.errorMsg(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return boardService.insertComment(insertComment);
    }

    @PatchMapping("/comment/{commentIdx}")
    public String updateComment(@PathVariable Long commentIdx,
                                           @Validated BoardReqDto.UpdateComment updateComment, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            //first error
            FieldError fieldError = errors.getFieldErrors().get(0);
            Helper.errorMsg(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return boardService.updateComment(commentIdx, updateComment);
    }

    @DeleteMapping("/comment/{commentIdx}")
    public String deleteComment(@PathVariable Long commentIdx) {
        return boardService.deleteComment(commentIdx);
    }
}
