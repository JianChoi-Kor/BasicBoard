package com.basic.board.domain.main;

import com.basic.board.domain.PageRequest;
import com.basic.board.domain.board.BoardResDto;
import com.basic.board.domain.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@RequestMapping(value = "/")
@Controller
public class MainController {

    private final MainService mainService;
    private final BoardService boardService;

    @GetMapping(value = {"main", ""})
    public String main(Model model) {
        PageImpl<BoardResDto.BoardForList> boardList = boardService.boardList(new PageRequest());
        model.addAttribute(boardList);
        return "page/board-main";
    }

    @ResponseBody
    @GetMapping("auth")
    public ResponseEntity<?> authInfo() {
        return mainService.authInfo();
    }
}
