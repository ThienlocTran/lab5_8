package com.thienloc.jakarta.lab58.controller;

import com.thienloc.jakarta.lab58.DAO.VideoDAO;
import com.thienloc.jakarta.lab58.DAOImpl.VideoDAOImpl;
import com.thienloc.jakarta.lab58.entity.Video;
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
        "/video/crud"
        ,"/video/crud/index"
        ,"/video/crud/edit/*"
        ,"/video/crud/create"
        ,"/video/crud/update"
        ,"/video/crud/delete"
        , "/video/crud/reset"
})
public class VideoCRUDServlet extends HttpServlet {
    private VideoDAO videoDAO = new VideoDAOImpl();

    @Override
    protected  void service (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Video form = new Video();
        try{
             BeanUtils.populate(form,request.getParameterMap());
        }catch (IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }

        String message ="Enter video Information";
        String path = request.getServletPath();
        if(path.contains("edit")){
            String id = request.getPathInfo().substring(1);
            form = videoDAO.findById(id);
            message = "Edit " + id;
        }else if(path.contains("create")){
            message = "Create " + form.getId();
            videoDAO.create(form);
            form = new Video();
        }else if(path.contains("update")){
            message = "Update " + form.getId();
            videoDAO.update(form);
        }else if(path.contains("delete")){
            message = "Delete " + form.getId();
            videoDAO.deleteById(form.getId());
            form = new Video();
        }else if(path.contains("reset")){
            message = "Reset";
            form = new Video();
        }
        List<Video> list = videoDAO.findAll();

        request.setAttribute("message",message);
        request.setAttribute("video",form);
        request.setAttribute("videos",list);
        request.getRequestDispatcher("/WEB-INF/video-crud.jsp").forward(request,response);
    }

}
