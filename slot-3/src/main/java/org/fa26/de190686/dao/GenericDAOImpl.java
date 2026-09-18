package org.fa26.de190686.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.fa26.de190686.util.JPAutil;

import java.util.List;

public class GenericDAOImpl<T,ID> implements GenericDAO<T,ID> {

    @Override
    public T save(T entity) {
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(entity);
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if(transaction.isActive())
                transaction.rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    @Override
    public T update(T entity) {
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T updatedEntity = em.merge(entity);
            transaction.commit();
            return updatedEntity;
        } catch (Exception e) {
            if(transaction.isActive())
                transaction.rollback();
            throw e;
        }finally {
            em.close();
        }
    }

    @Override
    public void delete(T entity) {
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T deletedEntity = em.merge(entity);
            em.remove(deletedEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive())
                transaction.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(Class<T> clazz, ID id) {
        EntityManager em = JPAutil.getEntityManager();

        try {

            T entity = em.find(clazz, id);

            return entity;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll(Class<T> clazz) {
        EntityManager em = JPAutil.getEntityManager();
        try {

            String jpql = "SELECT e FROM " + clazz.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql,clazz);
            List<T> entities = query.getResultList();

            return entities;
        } finally {
            em.close();
        }
    }

}
