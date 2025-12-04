package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.FavoriteDAO;
import com.thienloc.jakarta.lab58.DAOImpl.FavoriteDAOImpl;
import com.thienloc.jakarta.lab58.entity.Favorite;
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
        "/favorite/crud"
        ,"/favorite/crud/index"
        ,"/favorite/crud/edit/*"
        ,"/favorite/crud/create"
        ,"/favorite/crud/update"
        ,"/favorite/crud/delete"
        , "/favorite/crud/reset"
})
public class FavoriteCRUDServlet extends HttpServlet {
    private FavoriteDAO favoriteDAO = new FavoriteDAOImpl();

    @Override
    protected  void service (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Favorite form = new Favorite();
        try{
             BeanUtils.populate(form,request.getParameterMap());
        }catch (IllegalAccessException | InvocationTargetException | ConversionException e){
            e.printStackTrace();
        }

        String message ="Enter favorite Information";
        String path = request.getServletPath();
        if(path.contains("edit")){
            String id = request.getPathInfo().substring(1);
            form = favoriteDAO.findById(Long.parseLong(id));
            message = "Edit " + id;
        }else if(path.contains("create")){
            message = "Create " + form.getId();
            favoriteDAO.create(form);
            form = new Favorite();
        }else if(path.contains("update")){
            message = "Update " + form.getId();
            favoriteDAO.update(form);
        }else if(path.contains("delete")){
            message = "Delete " + form.getId();
            favoriteDAO.deleteById(form.getId());
            form = new Favorite();
        }else if(path.contains("reset")){
            message = "Reset";
            form = new Favorite();
        }
        List<Favorite> list = favoriteDAO.findAll();

        request.setAttribute("message",message);
        request.setAttribute("favorite",form);
        request.setAttribute("favorites",list);
        request.getRequestDispatcher("/WEB-INF/favorite-crud.jsp").forward(request,response);
    }

}
