package com.petriuk.web;

import com.petriuk.dto.UserDto;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class UsersListTag implements Tag {
    private PageContext pageContext;
    private Tag parent;
    private List<UserDto> users;

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    @Override
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    @Override
    public Tag getParent() {
        return parent;
    }

    @Override
    public void setParent(Tag parent) {
        this.parent = parent;
    }

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            out.println("<tbody>");
            for (UserDto user : users) {
                out.println("<tr>");
                out.println("<td>" + user.getLogin() + "</td>");
                out.println("<td>" + user.getFirstName() + "</td>");
                out.println("<td>" + user.getLastName() + "</td>");
                int age = Period.between(user.getBirthday().toLocalDate(), LocalDate.now()).getYears();
                out.println("<td>" + age + "</td>");
                out.println("<td>" + user.getRole() + "</td>");
                out.println("<td>");
                out.println("<a href=/admin/edituser?login=" + user.getLogin() + ">Edit</a>");
                out.println("<a href=# class=\"deleteUser\" data-userLogin=\"" + user.getLogin() + "\">Delete</a>");
                out.println("</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return 0;
    }

    @Override
    public void release() {

    }
}
