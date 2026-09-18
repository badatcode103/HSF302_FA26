package org.example.hsf302_classproject_1.dao;

import java.util.List;

public interface GenericDAO<T,ID> {
    T create(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(Class<T> clazz, ID id);
    List<T> findAll(Class<T> clazz);;
}
