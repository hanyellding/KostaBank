package com.my.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.service.BoardService;
import com.my.vo.Board;

@WebServlet(name = "BoardDetailServlet",value = "/boarddetail")
public class BoardDetailServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        BoardService service = new BoardService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        String board_id = request.getParameter("board_id");
        HttpSession session = request.getSession();
        session.setAttribute("board_id", board_id);

        try {
            Board board = service.boardById(board_id);
            jacksonMap.put("board_id",board.getBoard_id());
            jacksonMap.put("user_nickname",board.getUser().getUser_nickname());
            jacksonMap.put("board_subtitle",board.getBoard_subtitle());
            jacksonMap.put("board_title",board.getBoard_title());
            jacksonMap.put("board_content",board.getBoard_content());
            jacksonMap.put("board_wdate",board.getBoard_wdate());
            jacksonMap.put("board_view",board.getBoard_view());
            jacksonMap.put("board_file",board.getBoard_file());
            jacksonMap.put("board_up", board.getBoard_up());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (FindException e) {
            e.printStackTrace();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        }
    }

}
