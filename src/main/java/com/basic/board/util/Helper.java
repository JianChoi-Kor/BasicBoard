package com.basic.board.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class Helper {

    public static void errorMsg(String msg) {
        //getResponse
        HttpServletResponse response = ContextUtil.getResponse();
        response.setContentType("text/html; charset=UTF-8");

        try {
            PrintWriter out = response.getWriter();
            out.println("<script>alert('" + msg + "'); history.back(); </script>");
            out.close();
        } catch (IOException e) {
            log.info("IOException", e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void errorMsg(String field, String msg) {
        //getResponse
        HttpServletResponse response = ContextUtil.getResponse();
        response.setContentType("text/html; charset=UTF-8");

        try {
            PrintWriter out = response.getWriter();
            out.println("<script>document.getElementById('" + field + "').focus(); alert('" + msg + "'); history.back(); </script>");
            out.close();
        } catch (IOException e) {
            log.info("IOException", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
