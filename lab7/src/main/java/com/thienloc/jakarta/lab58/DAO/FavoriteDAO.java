package com.thienloc.jakarta.lab58.DAO;

import com.thienloc.jakarta.lab58.entity.Favorite;

import java.util.List;

public interface FavoriteDAO {
    List<Favorite> findAll();
    Favorite findById(long id);
    void create(Favorite item);
    void update(Favorite item);
    void deleteById(long id);
}
