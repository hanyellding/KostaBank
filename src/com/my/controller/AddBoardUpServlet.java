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
import com.my.exception.AddException;
import com.my.exception.ModifyException;
import com.my.service.BoardService;

/**
 * Servlet implementation class AddBoardUpServlet
 */
@WebServlet("/boardup")
public class AddBoardUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		Map <String, Object> jacksonMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		BoardService service = new BoardService();

		String board_id = request.getParameter("board_id");
		HttpSession session = request.getSession();
		String user_id = (String)request.getAttribute("loginInfo");
		try {
			service.addBoardUp(board_id, user_id);
			jacksonMap.put("status", 1);
			String jsonStr = mapper.writeValueAsString(jacksonMap);
			out.print(jacksonMap);
		} catch (AddException e1) {
			e1.printStackTrace();
			jacksonMap.put("status", -1);
			jacksonMap.put("msg", e1.getMessage());
			String jsonStr = mapper.writeValueAsString(jacksonMap);
			out.print(jsonStr);
		}
	}
}
