package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.FavoriteDAO;
import com.thienloc.jakarta.lab58.DAO.ShareDAO;
import com.thienloc.jakarta.lab58.DAO.UserDAO;
import com.thienloc.jakarta.lab58.DAO.VideoDAO;
import com.thienloc.jakarta.lab58.DAOImpl.FavoriteDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.ShareDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.UserDAOImpl;
import com.thienloc.jakarta.lab58.DAOImpl.VideoDAOImpl;
import com.thienloc.jakarta.lab58.entity.Favorite;
import com.thienloc.jakarta.lab58.entity.Share;
import com.thienloc.jakarta.lab58.entity.User;
import com.thienloc.jakarta.lab58.entity.Video;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet({
        "/video/list",
        "/video/detail/*",
        "/video/like/*",
        "/video/share/*"})
public class WatchVideo extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        String pathInfo = request.getPathInfo();

        response.setContentType("text/html;charset=UTF-8");

        if (path.equals("/video/list")) {
            response.getWriter().println("<h1>Video List Page</h1>");

        } else if (path.equals("/video/detail/*")) {
            String videoId = pathInfo != null ? pathInfo.substring(1) : "";
            response.getWriter().println("<h1>Video Detail Page</h1>");
            response.getWriter().println("<p>Video ID: " + videoId + "</p>");

        } else if (path.equals("/video/like/*")) {
            String videoId = pathInfo != null ? pathInfo.substring(1) : "";
            response.getWriter().println("<h1>Video Like Page</h1>");
            response.getWriter().println("<p>Video ID: " + videoId + "</p>");

        } else if (path.equals("/video/share/*")) {
            String videoId = pathInfo != null ? pathInfo.substring(1) : "";
            response.getWriter().println("<h1>Video Share Page</h1>");
            response.getWriter().println("<p>Video ID: " + videoId + "</p>");
        }
    }
}
