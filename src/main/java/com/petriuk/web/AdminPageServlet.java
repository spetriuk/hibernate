package com.petriuk.web;

import com.petriuk.service.UserService;
import com.petriuk.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/main")
public class AdminPageServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        try {
            List<UserDto> usersDtoList = userService.getAllUsersAsDto();
            req.setAttribute("users", usersDtoList);
        } catch (PersistenceException e) {
            LOG.error(e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        req.getRequestDispatcher("/WEB-INF/view/admin/main.jsp").forward(req, resp);
    }
}
