package com.petriuk.dto;

import com.petriuk.entity.User;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class UserDto {
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthday;
    private String role;

    public UserDto(User user) {
        login = user.getLogin();
        email= user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        birthday = user.getBirthday();
        role = user.getRole().getName();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Map<String, String> getUserDtoAsMap() {
        Map<String, String> user = new HashMap<>();
        user.put("login", login);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("birthday", birthday.toString());
        user.put("role", role);
        return user;
    }

    @Override
    public String toString() {
        return "login='" + login + '\'' + ", role='" + role + "'";
    }
}
