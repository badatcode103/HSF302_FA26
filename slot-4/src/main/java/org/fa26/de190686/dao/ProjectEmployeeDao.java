package org.fa26.de190686.dao;

import jakarta.persistence.EntityManager;
import org.fa26.de190686.pojo.Employee;
import org.fa26.de190686.pojo.Project;
import org.fa26.de190686.util.JPAutil;

import java.math.BigDecimal;

public class ProjectEmployeeDao {
    // ☐ TODO 5.6 — Viết EmployeeDAO với method assignEmployeeToProject(Long
    // employeeId, Long projectId): find cả 2 entity trong 1 transaction rồi gọi
    // assignToProject().
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
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return "Assign employee to project failed";
        } finally {
            entityManager.close();
        }
    }

    // ☐ TODO 5.8 — Viết JPQL đếm số nhân viên active tham gia mỗi project và tính
    // tổng salary của các nhân viên đó
    public void countActiveEmployeeAndCalculateSumSalary() {
        EntityManager em = JPAutil.getEntityManager();
        try {
            String jpql = """
                    SELECT p.projectName,
                    p.projectCode,
                    COUNT(e),
                    SUM(e.salary)
                    FROM Project p LEFT JOIN p.employees e ON e.active = true
                    GROUP BY p.id, p.projectCode, p.projectName
                    """;

            var results = em.createQuery(jpql, Object[].class).getResultList();

            for (Object[] row : results) {
                String projectName = (String) row[0];
                String projectCode = (String) row[1];
                Long totalEmployee = (Long) row[2];
                BigDecimal totalSalary = row[3] == null ? BigDecimal.ZERO : (BigDecimal) row[3];
                System.out.printf("%s - %s: active employee = %d, total salary = %s%n", projectCode, projectName,
                        totalEmployee, totalSalary);
            }

        } finally {
            em.close();
        }

    }
}
