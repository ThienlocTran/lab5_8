package com.thienloc.jakarta.lab58.filter;

import com.thienloc.jakarta.lab58.DAO.LogsDAO;
import com.thienloc.jakarta.lab58.DAOImpl.LogsDAOImpl;
import com.thienloc.jakarta.lab58.entity.Logs;
import com.thienloc.jakarta.lab58.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

@WebFilter(urlPatterns = "/*", filterName = "AppFilter")
public class AppFilter implements Filter {
    private LogsDAO logsDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logsDAO = new LogsDAOImpl();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("text/html; charset=UTF-8");
        
        String requestUri = request.getRequestURI();
        String username = null;
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                username = user.getId();
            }
        }
        
        if (username != null) {
            Logs logs = new Logs(requestUri, LocalDateTime.now(), username);
            logsDAO.create(logs);
            System.out.println("✓ Logged access: " + username + " -> " + requestUri);
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
