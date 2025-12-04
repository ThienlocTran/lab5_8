package com.thienloc.jakarta.lab58.DAOImpl;

import com.thienloc.jakarta.lab58.DAO.LogsDAO;
import com.thienloc.jakarta.lab58.XJPA;
import com.thienloc.jakarta.lab58.entity.Logs;
import jakarta.persistence.EntityManager;

public class LogsDAOImpl implements LogsDAO {
    @Override
    public void create(Logs logs) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(logs);
            em.getTransaction().commit();
        } catch(Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
