package com.basic.board.domain.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping(value = {"/main", "/"})
@Controller
public class MainController {

    @GetMapping("")
    public String main() {
        return "page/sign-in";
    }

    @GetMapping("404")
    public String notFound() {
        return "page/not-found";
    }

    @GetMapping("500")
    public String serverError() {
        return "page/server-error";
    }
}
