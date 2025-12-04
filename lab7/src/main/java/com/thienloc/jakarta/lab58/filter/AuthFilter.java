package com.thienloc.jakarta.lab58.filter;

import com.thienloc.jakarta.lab58.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {
        "/admin/*",
        "/account/change-password",
        "/account/edit-profile",
        "/video/like/*",
        "/video/share/*"
})
public class AuthFilter implements Filter {
    public static final String SECURITY_URI = "security_Uri ";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp =(HttpServletResponse) response;
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String uri = req.getRequestURI();
        String servletPath = req.getServletPath();
        
        // Nếu chưa login
        if(user == null){
            session.setAttribute(SECURITY_URI, uri);
            resp.sendRedirect(req.getContextPath() + "/account/sign-in");
        }
        // Nếu truy cập /admin/* mà không phải admin
        else if(servletPath.startsWith("/admin/") && !user.isAdmin()){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You do not have permission to access this resource");
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
