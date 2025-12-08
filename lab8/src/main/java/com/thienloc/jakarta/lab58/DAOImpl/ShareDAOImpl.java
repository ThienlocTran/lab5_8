package com.thienloc.jakarta.lab58.DAOImpl;

import com.thienloc.jakarta.lab58.DAO.ShareDAO;
import com.thienloc.jakarta.lab58.util.XJPA;
import com.thienloc.jakarta.lab58.entity.Share;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ShareDAOImpl implements ShareDAO {

    @Override
    public List<Share> findAll() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "select s from Share s";
            TypedQuery<Share> query = em.createQuery(jpql, Share.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Share findById(long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.find(Share.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Share item) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Share item) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(item);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            Share share = em.find(Share.class, id);
            if (share != null) {
                em.remove(share);
            }
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
