package com.basic.board.domain.main;

import com.basic.board.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping(value = "/")
@Controller
public class MainController {

    @GetMapping(value = {"main", ""})
    public String main() {
        return "page/board-main";
    }
}
