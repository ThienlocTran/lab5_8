package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.ShareDAO;
import com.thienloc.jakarta.lab58.DAOImpl.ShareDAOImpl;
import com.thienloc.jakarta.lab58.entity.Share;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet({
        "/share/crud"
        ,"/share/crud/index"
        ,"/share/crud/edit/*"
        ,"/share/crud/create"
        ,"/share/crud/update"
        ,"/share/crud/delete"
        , "/share/crud/reset"
})
public class ShareCRUDServlet extends HttpServlet {
    private ShareDAO shareDAO = new ShareDAOImpl();

    @Override
    protected  void service (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Share form = new Share();
        try{
             BeanUtils.populate(form,request.getParameterMap());
        }catch (IllegalAccessException | InvocationTargetException | ConversionException e){
            e.printStackTrace();
        }

        String message ="Enter share Information";
        String path = request.getServletPath();
        if(path.contains("edit")){
            String id = request.getPathInfo().substring(1);
            form = shareDAO.findById(Long.parseLong(id));
            message = "Edit " + id;
        }else if(path.contains("create")){
            message = "Create " + form.getId();
            shareDAO.create(form);
            form = new Share();
        }else if(path.contains("update")){
            message = "Update " + form.getId();
            shareDAO.update(form);
        }else if(path.contains("delete")){
            message = "Delete " + form.getId();
            shareDAO.deleteById(form.getId());
            form = new Share();
        }else if(path.contains("reset")){
            message = "Reset";
            form = new Share();
        }
        List<Share> list = shareDAO.findAll();

        request.setAttribute("message",message);
        request.setAttribute("share",form);
        request.setAttribute("shares",list);
        request.getRequestDispatcher("/WEB-INF/share-crud.jsp").forward(request,response);
    }

}
