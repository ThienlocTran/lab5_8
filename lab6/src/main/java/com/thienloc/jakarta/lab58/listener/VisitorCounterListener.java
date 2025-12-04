package com.thienloc.jakarta.lab58.listener;

import com.thienloc.jakarta.lab58.DAO.VisitorDAO;
import com.thienloc.jakarta.lab58.DAOImpl.VisitorDAOImpl;
import com.thienloc.jakarta.lab58.entity.Visitor;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class VisitorCounterListener implements HttpSessionListener, ServletContextListener {

    private VisitorDAO visitorDAO = new VisitorDAOImpl();

    @Override
    public void contextInitialized(ServletContextEvent sce){
        ServletContext context = sce.getServletContext();
        Visitor visitor = visitorDAO.getVisitorCount();
        int count = 0;
        if (visitor != null) {
            count = visitor.getVisitorCount();
        }
        context.setAttribute("visitors", count);
        System.out.println("✓ VisitorCounter initialized. Current visitors: " + count);
    }

    @Override
    public void sessionCreated(HttpSessionEvent e){
        ServletContext context = e.getSession().getServletContext();

        Integer countVisitors = (Integer) context.getAttribute("visitors");
        if(countVisitors == null) {
            countVisitors = 0;
        }
        countVisitors++;
        context.setAttribute("visitors", countVisitors);
        visitorDAO.updateVisitorCount(countVisitors);
        System.out.println("✓ New session created. Total visitors: " + countVisitors);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce){
        System.out.println("✓ Application stopped. Visitor count saved to database.");
    }   

}
