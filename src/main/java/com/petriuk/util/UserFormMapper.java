package com.petriuk.util;

import com.petriuk.entity.Role;
import com.petriuk.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class UserFormMapper {
    public static Map<String, String> getUserFormAsMap(HttpServletRequest req) {
        Map<String, String> params = new HashMap<>();
        params.put("login", req.getParameter("login"));
        params.put("password", req.getParameter("password"));
        params.put("password2", req.getParameter("password2"));
        params.put("email", req.getParameter("email"));
        params.put("firstName", req.getParameter("firstName"));
        params.put("lastName", req.getParameter("lastName"));
        params.put("birthday", req.getParameter("birthday"));
        params.put("role", req.getParameter("role"));
        return params;
    }

    public static User createUserFromForm(Map<String, String> form) {
        User user = new User();
        user.setLogin(form.get("login"));
        user.setPassword(form.get("password"));
        user.setEmail(form.get("email"));
        user.setFirstName(form.get("firstName"));
        user.setLastName(form.get("lastName"));
        user.setBirthday(Date.valueOf(form.get("birthday")));
        Role role = new Role();
        role.setName(form.get("role"));
        user.setRole(role);
        return user;
    }
}
