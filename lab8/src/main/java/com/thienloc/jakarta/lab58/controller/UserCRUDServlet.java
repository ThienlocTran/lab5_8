package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.UserDAO;
import com.thienloc.jakarta.lab58.DAOImpl.UserDAOImpl;
import com.thienloc.jakarta.lab58.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet({
        "/user/crud"
        ,"/user/crud/index"
        ,"/user/crud/edit/*"
        ,"/user/crud/create"
        ,"/user/crud/update"
        ,"/user/crud/delete"
        , "/user/crud/reset"
})
public class UserCRUDServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    protected  void service (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        User form = new User();
        try{
             BeanUtils.populate(form,request.getParameterMap());
        }catch (IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }

        String message ="Enter user Information";
        String path = request.getServletPath();
        if(path.contains("edit")){
            String id = request.getPathInfo().substring(1);
            form = userDAO.findById(id);
            message = "Edit " + id;
        }else if(path.contains("create")){
            message = "Create " + form.getId();
            userDAO.create(form);
            form = new User();
        }else if(path.contains("update")){
            message = "Update " + form.getId();
            userDAO.update(form);
        }else if(path.contains("delete")){
            message = "Delete " + form.getId();
            userDAO.deleteById(form.getId());
            form = new User();
        }else if(path.contains("reset")){
            message = "Reset";
            form = new User();
        }
        List<User> list = userDAO.findAll();

        request.setAttribute("message",message);
        request.setAttribute("user",form);
        request.setAttribute("users",list);
        request.getRequestDispatcher("/WEB-INF/user-crud.jsp").forward(request,response);
    }

}
