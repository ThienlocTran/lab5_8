package com.thienloc.jakarta.lab58.DAOImpl;

import com.thienloc.jakarta.lab58.DAO.FavoriteDAO;
import com.thienloc.jakarta.lab58.util.XJPA;
import com.thienloc.jakarta.lab58.entity.Favorite;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class FavoriteDAOImpl implements FavoriteDAO {

    @Override
    public List<Favorite> findAll() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "select f from Favorite f";
            TypedQuery<Favorite> query = em.createQuery(jpql, Favorite.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Favorite findById(long id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.find(Favorite.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public void create(Favorite item) {
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
    public void update(Favorite item) {
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
            Favorite favorite = em.find(Favorite.class, id);
            if (favorite != null) {
                em.remove(favorite);
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
