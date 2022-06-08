package com.basic.board.domain.board;

import com.basic.board.advice.Response;
import com.basic.board.domain.PageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
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
    public ResponseEntity<?> boardList(@Validated BoardReqDto.SearchBoard search, Errors searchErrors,
                                       PageRequest pageRequest) {
        //valid check
        if (searchErrors.hasErrors()) {
            //first error
            return response.validResponse(searchErrors);
        }

        //type && keyword check
        if ((search.getKeyword() != null && !search.getKeyword().equals("")) && search.getType() == null) {
            return response.fail("잘못된 검색 조건입니다.");
        }
        if (search.getType() != null && search.getKeyword() == null) {
            return response.fail("잘못된 검색 조건입니다.");
        }
        List<String> typeList = Arrays.asList("title", "contents", "titleAndContents");
        if (search.getType() !=  null && !typeList.contains(search.getType())) {
            return response.fail("잘못된 검색 조건입니다.");
        }

        //startDate, endDate check
        if (search.getStartDate() != null && search.getEndDate() != null) {
            //convert startDate, endDate
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime startDate = LocalDate.parse(search.getStartDate(), dateTimeFormatter).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(search.getEndDate(), dateTimeFormatter).atTime(23, 59, 59);
            if (startDate.isAfter(endDate)) {
                return response.fail("잘못된 검색 조건입니다.");
            }
        }

        PageImpl<BoardResDto.BoardForList> boardList = boardService.boardList(search, pageRequest);
        return response.success(boardList);
    }

    @GetMapping("/{boardIdx}")
    public ResponseEntity<?> boardDetail(@PathVariable Long boardIdx) {
        BoardResDto.BoardDetail boardDetail = boardService.boardDetail(boardIdx);
        if (boardDetail == null) {
            return response.fail("잘못된 요청입니다.");
        }
        return response.success(boardDetail);
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
