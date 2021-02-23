package com.my.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.exception.ModifyException;
import com.my.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ModPwdByEmailServlet" , value = "/modpwdbyemail")
public class ModPwdByEmailServlet extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        UserService service = new UserService();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jacksonMap = new HashMap<>();

        String email = request.getParameter("user_email");

        try {
            service.modifyPwdByEmail(email);
            jacksonMap.put("status", 1);
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        } catch (ModifyException e) {
            e.printStackTrace();
            jacksonMap.put("status", -1);
            jacksonMap.put("msg", e.getMessage());
            String jsonStr = mapper.writeValueAsString(jacksonMap);
            out.print(jsonStr);
        }
    }
}
