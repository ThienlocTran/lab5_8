package com.thienloc.jakarta.lab58.DAOImpl;

import com.thienloc.jakarta.lab58.DAO.VisitorDAO;
import com.thienloc.jakarta.lab58.util.XJPA;
import com.thienloc.jakarta.lab58.entity.Visitor;
import jakarta.persistence.EntityManager;

public class VisitorDAOImpl implements VisitorDAO {
    @Override
    public Visitor getVisitorCount(){
        EntityManager em = XJPA.getEntityManager();
        try {
            Visitor visitor = em.find(Visitor.class, 1);
            if (visitor != null) {
                return visitor;
            }
            visitor = new Visitor();
            visitor.setId(1);
            visitor.setVisitorCount(0);
            em.getTransaction().begin();
            em.persist(visitor);
            em.getTransaction().commit();
            return visitor;
        } catch(Exception e) {
            e.printStackTrace();
            Visitor visitor = new Visitor();
            visitor.setId(1);
            visitor.setVisitorCount(0);
            return visitor;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateVisitorCount(int count){
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            
            Visitor visitor = em.find(Visitor.class, 1);
            if (visitor == null) {
                visitor = new Visitor();
                visitor.setId(1);
                visitor.setVisitorCount(count);
                em.persist(visitor);
            } else {
                visitor.setVisitorCount(count);
            }
            
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
