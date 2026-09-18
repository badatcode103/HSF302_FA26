package org.fa26.de190686.dao;

import jakarta.persistence.EntityManager;
import org.fa26.de190686.pojo.Project;
import org.fa26.de190686.util.JPAutil;

import java.util.Optional;

public class ProjectDao {
    public Optional<Project> findProjectById(Long projectId){
        EntityManager entityManager = JPAutil.getEntityManager();
        return Optional.ofNullable(entityManager.find(Project.class, projectId));
    }
}
