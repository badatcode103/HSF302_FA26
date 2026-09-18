package org.fa26.de190686.dao;

import jakarta.persistence.EntityManager;
import org.fa26.de190686.pojo.Employee;
import org.fa26.de190686.pojo.Project;
import org.fa26.de190686.util.JPAutil;

import java.util.Optional;

public class EmployeeDao {
    public Optional<Employee> findEmployeeById(Long employeeId) {
        EntityManager entityManager = JPAutil.getEntityManager();
        return Optional.ofNullable(entityManager.find(Employee.class, employeeId));
    }

    //☐	TODO 5.6 — Viết EmployeeDAO với method assignEmployeeToProject(Long employeeId, Long projectId): find cả 2 entity trong 1 transaction rồi gọi assignToProject().
    public String assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager entityManager = JPAutil.getEntityManager();
        try {
            entityManager.getTransaction().begin();
            Employee employee = entityManager.find(Employee.class, employeeId);
            Project project = entityManager.find(Project.class, projectId);
            employee.assignProject(project);
            entityManager.getTransaction().commit();
            return "Assign employee to project successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Assign employee to project failed";
        } finally {
            entityManager.close();
        }
    }
}
