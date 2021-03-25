package com.petriuk.web;

import com.petriuk.dto.UserDto;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("")
public class RootServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        HttpSession session = req.getSession();
        UserDto loggedUser = (UserDto) session.getAttribute("loggedUser");
        if (loggedUser == null) {
            resp.sendRedirect("/login");
        } else {
            resp.sendRedirect("/" + loggedUser.getRole() + "/main");
        }
    }
}
