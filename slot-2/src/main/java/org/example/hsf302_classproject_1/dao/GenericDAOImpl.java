package org.example.hsf302_classproject_1.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

public class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    @Override
    public T create(T entity) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(entity);
            tx.commit();
            return entity;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    };

    public T update(T entity) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T merged = em.merge(entity);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    };

    public void delete(T entity) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T deletedEntity = em.merge(entity);
            em.remove(deletedEntity);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    };

    public T findById(Class<T> clazz, ID id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.find(clazz, id);
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    };

    public List<T> findAll(Class<T> clazz){
        EntityManager em = JpaUtil.createEntityManager();
        try{
            String jqpl = "SELECT e FROM " + clazz.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jqpl,clazz);
            return query.getResultList();
        }catch(Exception e){
            throw e;
        }
        finally{
            em.close();
        }
    };
}
