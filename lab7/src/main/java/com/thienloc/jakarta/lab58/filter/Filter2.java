package com.thienloc.jakarta.lab58.filter;

import jakarta.servlet.*;

import java.io.IOException;

@jakarta.servlet.annotation.WebFilter(urlPatterns = "/*", filterName = "Filter2")
public class Filter2 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String hello = (String) servletRequest.getAttribute("hello");
        System.out.println("✓ Filter2 executed - getAttribute hello: " + hello);
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
