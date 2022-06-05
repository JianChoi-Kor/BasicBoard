package com.basic.board.domain.main;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@RequestMapping(value = "/")
@Controller
public class MainController {

    private final MainService mainService;

    @GetMapping(value = {"main", ""})
    public String main() {
        return "page/board-main";
    }

    @ResponseBody
    @GetMapping("auth")
    public ResponseEntity<?> authInfo() {
        return mainService.authInfo();
    }
}
