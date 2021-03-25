package com.petriuk.web;

import com.petriuk.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin/delete")
public class DeleteUserServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger();
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {
        try {
            if(!userService.deleteUser(req.getParameter("userLogin"))) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (PersistenceException e) {
            LOG.error(e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        resp.sendRedirect("main");
    }
}
