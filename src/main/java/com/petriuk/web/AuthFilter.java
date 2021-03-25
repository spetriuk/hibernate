package com.petriuk.web;

import com.petriuk.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger();
    private List<String> guestURI;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        guestURI = new ArrayList<>();
        guestURI.add("/");
        guestURI.add("/login");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        UserDto loggedUser = (UserDto) req.getSession().getAttribute("loggedUser");
        String reqURI = req.getRequestURI();

        if (loggedUser == null) {
            if (guestURI.contains(reqURI) || reqURI.startsWith("/static")) {
                chain.doFilter(request, response);
            } else {
                resp.sendRedirect("/login");
            }
        } else {
            if (reqURI.contains(loggedUser.getRole()) ||
                reqURI.equals("/logout") ||
                guestURI.contains(reqURI) ||
                reqURI.startsWith("/static")) {
                chain.doFilter(request, response);
            } else {
                LOG.debug("Unauthorized access attempt! " + loggedUser + " on " + reqURI);
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }

    @Override
    public void destroy() {
    }
}
