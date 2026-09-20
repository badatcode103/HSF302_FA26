package org.fa26.de190686.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.fa26.de190686.pojo.Employee;
import org.fa26.de190686.pojo.Project;
import org.fa26.de190686.util.JPAutil;

import java.math.BigDecimal;
import java.util.List;

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

    // ☐ TODO 5.9 — Viết method unassignFromProject(Project p) (gỡ khỏi dự án)
    // và demo gỡ 1 nhân viên khỏi 1 project — xác nhận bảng employee_project mất
    // đúng 1 dòng, không ảnh hưởng Employee/Project gốc.
    public boolean unassignFromProject(Project p, Long employeeId) {
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, p.getId());

            if (employee == null || project == null) {
                throw new IllegalArgumentException(
                        "Employee hoặc Project không tồn tại");

            }
            boolean removed = employee.getProjects().contains(project);
            if (removed) {
                employee.removeProject(project);
            }
            tx.commit();
            return removed;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // ☐ TODO 5.10 — Viết JPQL tìm các Employee (chỉ lấy active = true) đang tham
    // gia nhiều hơn 1 project cùng lúc
    public List<Employee> findActiveEmployeeJoinMoreThanOneProject() {
        EntityManager em = JPAutil.getEntityManager();
        try {
            String jpql = """
                    SELECT e
                    FROM Employee e
                    WHERE e.active = true
                    AND SIZE(e.projects) > 1
                    """;
            List<Employee> results = em.createQuery(jpql, Employee.class).getResultList();
            return results;
        } finally {
            em.close();
        }
    }

    // ☐ TODO 5.11 — Viết method deactivateEmployee(Long employeeId) (set active =
    // false) và giải thích trong comment:
    // nhân viên nghỉ việc có nên tự động bị gỡ khỏi tất cả project hay không, và
    // cách xử lý phù hợp (không cascade REMOVE tự động).
    public void deactivateEmployee(Long employeeId) {
        EntityManager em = JPAutil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Employee employee = em.find(Employee.class, employeeId);
            if (employee != null) {
                employee.setActive(false);
                // Giải thích: Khi một nhân viên nghỉ việc, không nên tự động gỡ khỏi tất cả các
                // dự án.
                // Lý do là thông tin về việc nhân viên đã tham gia dự án có thể vẫn cần được
                // lưu trữ cho mục đích báo cáo hoặc lịch sử.
                // Thay vào đó, chỉ cần đánh dấu nhân viên là inactive và giữ nguyên các liên
                // kết với dự án.
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
