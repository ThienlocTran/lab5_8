package com.thienloc.jakarta.lab58.DAO;

import com.thienloc.jakarta.lab58.entity.User;

import java.util.List;

public interface UserDAO {

    List<User> findAll();
    /**Truy vấn theo mã*/
    User findById(String id);
    /**Truy vấn theo ID hoặc Email*/
    User findByIdOrEmail(String idOrEmail);
    /**Thêm mới*/
    void create(User item);
    /**Cập nhật*/
    void update(User item);
    /**Xóa theo mã*/
    void deleteById(String id);
}
