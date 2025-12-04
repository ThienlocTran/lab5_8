package com.thienloc.jakarta.lab58.servlet;

import com.thienloc.jakarta.lab58.XJPA;
import com.thienloc.jakarta.lab58.entity.Logs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "LogsServlet", urlPatterns = "/logs")
public class LogsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.   setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        
        out.println("<html>");
        out.println("<head><title>Access Logs</title></head>");
        out.println("<body>");
        out.println("<h1>Access Logs</h1>");
        
        EntityManager em = XJPA.getEntityManager();
        try {
            Query query = em.createQuery("SELECT l FROM Logs l ORDER BY l.accessTime DESC");
            List<Logs> logsList = query.getResultList();
            
            out.println("<table border='1' cellpadding='10'>");
            out.println("<tr>");
            out.println("<th>Id</th>");
            out.println("<th>URL</th>");
            out.println("<th>Access Time</th>");
            out.println("<th>Username</th>");
            out.println("</tr>");
            
            if (logsList.isEmpty()) {
                out.println("<tr><td colspan='4'>No logs found</td></tr>");
            } else {
                for (Logs log : logsList) {
                    out.println("<tr>");
                    out.println("<td>" + log.getId() + "</td>");
                    out.println("<td>" + log.getUrl() + "</td>");
                    out.println("<td>" + log.getAccessTime() + "</td>");
                    out.println("<td>" + log.getUsername() + "</td>");
                    out.println("</tr>");
                }
            }
            
            out.println("</table>");
            out.println("<br><a href='/lab5-8/'>Back to Home</a>");
        } catch (Exception e) {
            out.println("<p>Error loading logs: " + e.getMessage() + "</p>");
            e.printStackTrace(out);
        } finally {
            em.close();
        }
        
        out.println("</body>");
        out.println("</html>");
    }
}
