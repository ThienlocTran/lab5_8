package com.thienloc.jakarta.lab58;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class XJPA {

    private static EntityManagerFactory factory;
    private static Exception initException;

    static {
        try {
            factory = Persistence.createEntityManagerFactory("PolyOE");
        } catch (Exception e) {
            initException = e;
            System.err.println("Failed to initialize EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static EntityManager getEntityManager() {
        if (factory == null) {
            throw new RuntimeException("EntityManagerFactory not initialized", initException);
        }
        return factory.createEntityManager();
    }
}