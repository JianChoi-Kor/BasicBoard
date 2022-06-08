package com.basic.board.domain.board;

import com.basic.board.advice.Response;
import com.basic.board.domain.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Api(tags = {"게시판"})
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class BoardController {

    private final Response response;
    private final BoardService boardService;

    @ApiOperation(value = "게시글 쓰기", notes = "게시글 쓰기 기능", authorizations = @Authorization(value = "Bearer"))
    @ResponseBody
    @PostMapping("/write")
    public ResponseEntity<?> insertBoard(@RequestBody @Validated BoardReqDto.InsertAndUpdate input, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.insertBoard(input);
    }

    @ApiOperation(value = "게시글 리스트 조회", notes = "게시글 리스트 조회 기능")
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

    @ApiOperation(value = "게시글 상세 조회", notes = "게시글 상세 조회 기능")
    @GetMapping("/{boardIdx}")
    public ResponseEntity<?> boardDetail(@PathVariable Long boardIdx) {
        BoardResDto.BoardDetail boardDetail = boardService.boardDetail(boardIdx);
        if (boardDetail == null) {
            return response.fail("잘못된 요청입니다.");
        }
        return response.success(boardDetail);
    }

    @ApiOperation(value = "게시글 수정", notes = "게시글 수정 기능", authorizations = @Authorization(value = "Bearer"))
    @PatchMapping("/update/{boardIdx}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardIdx,
                                         @Validated BoardReqDto.InsertAndUpdate input, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.updateBoard(boardIdx, input);
    }

    @ApiOperation(value = "게시글 삭제", notes = "게시글 삭제 기능", authorizations = @Authorization(value = "Bearer"))
    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardIdx) {
        return boardService.deleteBoard(boardIdx);
    }

    @ApiOperation(value = "댓글 쓰기", notes = "댓글 쓰기 기능", authorizations = @Authorization(value = "Bearer"))
    @PostMapping("/comment")
    public ResponseEntity<?> insertComment(@Validated BoardReqDto.InsertComment insertComment, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.insertComment(insertComment);
    }

    @ApiOperation(value = "댓글 수정", notes = "댓글 수정 기능", authorizations = @Authorization(value = "Bearer"))
    @PatchMapping("/comment/{commentIdx}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentIdx,
                                           @Validated BoardReqDto.UpdateComment updateComment, Errors errors) {
        //valid check
        if (errors.hasErrors()) {
            return response.validResponse(errors);
        }
        return boardService.updateComment(commentIdx, updateComment);
    }

    @ApiOperation(value = "댓글 삭제", notes = "댓글 삭제 기능", authorizations = @Authorization(value = "Bearer"))
    @DeleteMapping("/comment/{commentIdx}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentIdx) {
        return boardService.deleteComment(commentIdx);
    }
}
