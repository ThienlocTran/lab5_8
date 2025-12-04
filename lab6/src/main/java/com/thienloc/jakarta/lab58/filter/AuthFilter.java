package com.thienloc.jakarta.lab58.filter;

import com.thienloc.jakarta.lab58.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter({
        "/admin/*",
        "/account/change-password",
        "/account/edit-profile",
        "/video/like/*",
"/video/share/*"}
)
public class AuthFilter implements Filter {
    public static final String SECURITY_URI = "security_Uri ";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp =(HttpServletResponse) response;
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String uri = req.getRequestURI();
        
        // Nếu chưa login
        if(user == null){
            session.setAttribute(SECURITY_URI, uri);
            resp.sendRedirect(req.getContextPath() + "/login");
        }
        // Nếu truy cập /admin/* mà không phải admin
        else if(uri.contains("/admin/") && !user.isAdmin()){
            session.setAttribute(SECURITY_URI, uri);
            resp.sendRedirect(req.getContextPath() + "/login");
        }
        // Nếu có quyền
        else{
            chain.doFilter(request, response);
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
}
