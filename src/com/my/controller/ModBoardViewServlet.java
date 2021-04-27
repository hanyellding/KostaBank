package com.my.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.ModifyException;
import com.my.service.BoardService;

/**
 * Servlet implementation class ModBoardViewServlet
 */
@WebServlet("/modboardview")
public class ModBoardViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String board_id = request.getParameter("board_id");
		BoardService service = new BoardService();
		try {
			service.modifyBoardView(board_id);
		} catch (ModifyException e) {
			e.printStackTrace();
		}
	}

}
