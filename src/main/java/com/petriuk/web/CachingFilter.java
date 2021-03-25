package com.petriuk.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class CachingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Cache-Control", "no-cache, no-store");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
