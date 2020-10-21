package com.linkedin.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", asyncSupported = true)
public class ParameterLoggingFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        req.getParameterMap().entrySet().stream()
                .forEach(stringEntry -> System.out.println(String.format("%s: %s", stringEntry.getKey(), stringEntry.getValue()[0])));
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
