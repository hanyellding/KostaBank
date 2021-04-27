package com.my.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.AddException;
import com.my.service.QuestionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AddRandomQTmpServlet",value = "/addrandomqtmp")
public class AddRandomQTmpServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        QuestionService Service = new QuestionService();
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> jacksonMap = new HashMap<>();
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("loginInfo");

        try {
            Service.insertRandomQTmp(id);
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
