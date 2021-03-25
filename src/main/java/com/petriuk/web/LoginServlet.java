package com.petriuk.web;

import com.petriuk.service.UserService;
import com.petriuk.dto.UserDto;
import com.petriuk.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOG = LogManager.getLogger();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String login = req.getParameter("login");
        String pass = req.getParameter("password");
        LOG.debug("Trying to log in with login '" + login + "'");
        try {
            Optional<User> user = userService.checkUser(login, pass);
            if (user.isPresent()) {
                String role = user.get().getRole().getName();
                loginUser(req.getSession(), user.get());
                resp.sendRedirect("/" + role + "/main");
            } else {
                LOG.debug("User '" + login + "' not logged in");
                req.setAttribute("login", login);
                req.setAttribute("error", "Wrong login or password");
                req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
            }
        } catch (PersistenceException e) {
            LOG.error(e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void loginUser(HttpSession session, User user) {
        UserDto loggedUser = new UserDto(user);
        session.setAttribute("loggedUser", loggedUser);
        LOG.debug("User " + loggedUser + " has been logged in");
    }

}
