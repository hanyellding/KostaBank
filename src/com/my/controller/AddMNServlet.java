package com.my.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.AddException;
import com.my.service.QuestionService;
import jdk.nashorn.internal.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AddMNServlet",value = "/addmn")
public class AddMNServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        QuestionService service = new QuestionService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");
//        JSONParser jsonParser = new JSONParser();
        String[] question_id_list = request.getParameterValues("question_id_list");
        try {
            service.addMnById(id,question_id_list);
            jacksonMap.put("status", 1);
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (AddException e) {
            e.printStackTrace();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg",e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        }
    }

}
