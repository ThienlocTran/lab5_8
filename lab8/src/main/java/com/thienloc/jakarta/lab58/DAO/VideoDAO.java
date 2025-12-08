package com.thienloc.jakarta.lab58.DAO;

import com.thienloc.jakarta.lab58.entity.Video;

import java.util.List;

public interface VideoDAO {
    List<Video> findAll();
    Video findById(String id);
    void create(Video item);
    void update(Video item);
    void deleteById(String id);
}
