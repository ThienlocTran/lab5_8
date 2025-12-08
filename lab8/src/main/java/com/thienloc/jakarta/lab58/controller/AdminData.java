package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.FavoriteDAO;
import com.thienloc.jakarta.lab58.DAO.ShareDAO;
import com.thienloc.jakarta.lab58.DAO.UserDAO;
import com.thienloc.jakarta.lab58.DAO.VideoDAO;
import com.thienloc.jakarta.lab58.DAOImpl.FavoriteDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.ShareDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.UserDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.VideoDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet({
        "/admin/video",
        "/admin/user",
        "/admin/like",
        "/admin/sha"
})
public class AdminData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();

        response.setContentType("text/html;charset=UTF-8");

        if (path.equals("/admin/video")) {
            response.getWriter().println("<h1>Admin Video Management Page</h1>");

        } else if (path.equals("/admin/user")) {
            response.getWriter().println("<h1>Admin User Management Page</h1>");

        } else if (path.equals("/admin/like")) {
            response.getWriter().println("<h1>Admin Like Management Page</h1>");

        } else if (path.equals("/admin/sha")) {
            response.getWriter().println("<h1>Admin Share Management Page</h1>");
        }
    }
}
