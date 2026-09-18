package org.example.hsf302_classproject_1.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
    private static final String PERSISTENCE_UNIT_NAME = "employee_manager";
    private static final EntityManagerFactory emf;

    static {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public static  EntityManager createEntityManager(){
        return emf.createEntityManager();
    }

    public static void shutdown(){
        if(emf != null)
            emf.close();
    }
}
