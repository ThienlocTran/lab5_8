package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.UserDAO;
import com.thienloc.jakarta.lab58.DAOImpl.UserDAOImpl;
import com.thienloc.jakarta.lab58.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;

@WebServlet({
        "/account/sign-in",
        "/account/change-password",
        "/account/edit-profile"
})
public class AccountSetting extends HttpServlet {
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = path.substring(contextPath.length());

        response.setContentType("text/html;charset=UTF-8");

        if (uri.equals("/account/sign-in")) {
            response.getWriter().println("<h1>Sign In Page</h1>");
        } else if (uri.equals("/account/change-password")) {
            response.getWriter().println("<h1>Change Password Page</h1>");
        } else if (uri.equals("/account/edit-profile")) {
            response.getWriter().println("<h1>Edit Profile Page</h1>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String path = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = path.substring(contextPath.length());
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        if (uri.equals("/account/change-password")) {
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");

            if (currentUser != null && currentUser.getPassword().equals(oldPassword)) {
                currentUser.setPassword(newPassword);
                userDAO.update(currentUser);
                session.setAttribute("user", currentUser);
                request.setAttribute("message", "Password changed successfully!");
            } else {
                request.setAttribute("message", "Old password is incorrect!");
            }
            request.getRequestDispatcher("/WEB-INF/change-password.jsp").forward(request, response);

        } else if (uri.equals("/account/edit-profile")) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");

            if (currentUser != null) {
                currentUser.setFullName(fullName);
                currentUser.setEmail(email);
                userDAO.update(currentUser);
                session.setAttribute("user", currentUser);
                request.setAttribute("message", "Profile updated successfully!");
            }
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("/WEB-INF/edit-profile.jsp").forward(request, response);
        }
    }
}

