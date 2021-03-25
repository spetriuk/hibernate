package com.petriuk.web;

import com.petriuk.service.UserService;
import com.petriuk.dto.UserDto;
import com.petriuk.entity.User;
import com.petriuk.util.Validator;

import javax.persistence.PersistenceException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.petriuk.util.UserFormMapper.createUserFromForm;
import static com.petriuk.util.UserFormMapper.getUserFormAsMap;

@WebServlet("/admin/edituser")
public class EditUserPageServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        try {
            Optional<User> user = userService
                .getUserByLogin(req.getParameter("login"));
            if (user.isPresent()) {
                req.setAttribute("user", new UserDto(user.get()).getUserDtoAsMap());
            } else {
                resp.sendRedirect("main");
            }
        } catch (PersistenceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        req.getRequestDispatcher("/WEB-INF/view/admin/edituser.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        Map<String, String> form = getUserFormAsMap(req);
        Map<String, String> errors = Validator
            .getErrors(form, !req.getParameter("password").isEmpty());

        if (!errors.isEmpty()) {
            req.setAttribute("user", form);
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/view/admin/edituser.jsp").forward(req, resp);
            return;
        }

        try {
            if (userService.updateUser(createUserFromForm(form),
                req.getParameter("username"))) {
                resp.sendRedirect("main");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (PersistenceException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
