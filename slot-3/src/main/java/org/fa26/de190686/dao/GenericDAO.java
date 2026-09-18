package org.fa26.de190686.dao;

import java.util.List;

public interface GenericDAO<T, ID> {
    T save(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(Class<T> clazz,ID id);
    List<T> findAll(Class<T> clazz);
}
