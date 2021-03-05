package com.my.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.FindException;
import com.my.exception.ModifyException;
import com.my.model.RenamePolicy;
import com.my.service.BoardService;
import com.my.vo.Board;
import com.my.vo.User;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * Servlet implementation class ModBoardServlet
 */
@WebServlet("/modboard")
public class ModBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		Map<String, Object> jacksonMap = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		//게시글 번호 
//		String board_id = request.getParameter("board_id");
//		System.out.println("글수정 보드아이디 : "  +board_id);
		//테스트용 게시글번호
		String board_id = "2";
		System.out.println("글수정 보드아이디 : "  +board_id);
		
		//로그인아이디
//		HttpSession session = request.getSession();
//		String loginedId = (String)session.getAttribute("loginInfo");
		
		
		try {
			//테스트용 아이디
			String loginedId = "rlaqhfka";
			BoardService service = new BoardService();
			 File uploadFile =  new File(this.getServletContext().getRealPath("/")+ "/boardupload");
		        if(!uploadFile.exists()){
		            uploadFile.mkdir();
		        }
			
			String saveDirectory = getServletContext().getRealPath("boardupload");
			int maxPostSize = 10*1024*1024;
			String encoding = "UTF-8";
			FileRenamePolicy policy = new RenamePolicy(board_id);
			MultipartRequest mr = new MultipartRequest(request, saveDirectory, maxPostSize, encoding, policy);
			
			//수정된 게시글 객체 (get)
			Board newBoard = new Board();
			String newSubtitle = mr.getParameter("subtitle"); //수정 소제목
			String newTitle = mr.getParameter("title"); //수정 제목
			String newContent = mr.getParameter("board_content");//수정 내용
			String newFile = mr.getOriginalFileName("board_file"); //수정파일
			
			
			Enumeration<String> e = mr.getFileNames(); //파일 이름들만 얻어옴
			while(e.hasMoreElements()) {
				String fileName = e.nextElement();
				System.out.println("fileName = " + fileName);
				System.out.println("OriginalFileName = " + mr.getOriginalFileName(fileName));
			}
			
			
			//수정 게시글 (set)
			newBoard.setBoard_subtitle(newSubtitle);
			newBoard.setBoard_title(newTitle);
			newBoard.setBoard_content(newContent);
			newBoard.setBoard_file(newFile);
			newBoard.setBoard_id(board_id);//글번호
			User user = new User();
			user.setUser_id(loginedId);
			newBoard.setUser(user);//로그인아이디 
			
			//수정 게시글 출력 
			System.out.println( "new: " + newBoard);
			System.out.println("-------------------");
			
			
			//기존 게시글 
			Board oldBoard = new Board();
//			BoardService service = new BoardService();
			
			oldBoard = service.boardById(board_id);
			String oldSubtitle = oldBoard.getBoard_subtitle();
			String oldTitle = oldBoard.getBoard_title();
			String oldContent = oldBoard.getBoard_content();
			String oldFile = oldBoard.getBoard_file();
			oldBoard.setUser(user);
			
			//기존게시글 출력
			System.out.println("old : " + oldBoard);
			
			if(!(oldSubtitle.equals(newSubtitle))) {
				oldBoard.setBoard_subtitle(newSubtitle);
			}
			if(!(oldTitle.equals(newTitle))) {
				oldBoard.setBoard_title(newTitle);
			}
			if(!(oldContent.equals(newContent))) {
				oldBoard.setBoard_content(newContent);
			}
			
			service.modifyBoard(oldBoard);
			
			jacksonMap.put("status", 1);
			String jsonStr = mapper.writeValueAsString(jacksonMap);
			out.print(jsonStr);
		} catch (FindException | ModifyException e) {
			e.printStackTrace();
			jacksonMap.put("status", -1);
            jacksonMap.put("msg",e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
		}
	}

}
