package com.thienloc.jakarta.lab58.DAOImpl;

import com.thienloc.jakarta.lab58.DAO.UserDAO;
import com.thienloc.jakarta.lab58.util.XJPA;
import com.thienloc.jakarta.lab58.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> findAll() {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "select u from User u";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public User findById(String id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public User findByIdOrEmail(String idOrEmail) {
        EntityManager em = XJPA.getEntityManager();
        try {
            String jpql = "select u from User u where u.id = :idOrEmail or u.email = :idOrEmail";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("idOrEmail", idOrEmail);
            try {
                return query.getSingleResult();
            } catch (Exception e) {
                return null;
            }
        } finally {
            em.close();
        }
    }

    @Override
    public void create(User user) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(User user) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch(Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteById(String id) {
        EntityManager em = XJPA.getEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
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
