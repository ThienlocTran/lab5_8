package com.thienloc.jakarta.lab58.DAO;

import com.thienloc.jakarta.lab58.entity.Share;

import java.util.List;

public interface ShareDAO {
    List<Share> findAll();
    Share findById(long id);
    void create(Share item);
    void update(Share item);
    void deleteById(long id);
}
