package com.thienloc.jakarta.lab58.controller;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class UserManager {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("PolyOE");
    EntityManager em = factory.createEntityManager();
    public void findAll(){}
    public void findById(){}
    public void create(){}
    public void update(){}
    public void deleteById(){}

}
