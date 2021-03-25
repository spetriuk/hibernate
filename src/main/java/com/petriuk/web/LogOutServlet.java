package com.petriuk.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        HttpSession session = req.getSession();
        LOG.debug("User " + session.getAttribute("loggedUser") + "has been logged out");
        session.invalidate();
        resp.sendRedirect("/");
    }
}
