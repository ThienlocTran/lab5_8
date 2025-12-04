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

@WebServlet({"/login", "/login/index"})
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idOrEmail = request.getParameter("idOrEmail");
        String password = request.getParameter("password");
        String message = "";

        User user = userDAO.findByIdOrEmail(idOrEmail);

        if (user == null) {
            message = "User not found!";
            request.setAttribute("message", message);
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } else if (!user.getPassword().equals(password)) {
            message = "Password incorrect!";
            request.setAttribute("message", message);
            request.getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            String previousUri = (String) session.getAttribute("SECURITY_URI");
            if (previousUri != null && !previousUri.isEmpty()) {
                session.removeAttribute("SECURITY_URI");
                response.sendRedirect(request.getContextPath() + previousUri);
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
        }
    }

}
