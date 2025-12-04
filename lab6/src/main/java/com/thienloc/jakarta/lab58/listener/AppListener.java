package com.thienloc.jakarta.lab58.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppListener  implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent sce){
        ServletContext context = sce.getServletContext();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce){

        ServletContext context = sce.getServletContext();
    }
}
