package com.thienloc.jakarta.lab58.DAO;

import com.thienloc.jakarta.lab58.entity.Visitor;

public interface VisitorDAO {


    Visitor getVisitorCount();
    void updateVisitorCount(int count);
}

