package org.fa26.de190686;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.fa26.de190686.pojo.Employee;
import org.fa26.de190686.pojo.Gender;
import org.fa26.de190686.pojo.Project;
import org.fa26.de190686.util.JPAutil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class Hsf302Slot4Application {

    public static void main(String[] args) {
        EntityManager entityManager = JPAutil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Project projectA = createProject(
                    "PRJ-A",
                    "Project A",
                    new BigDecimal("500000000"),
                    LocalDate.of(2026, 9, 1),
                    null
            );
            Project projectB = createProject(
                    "PRJ-B",
                    "Project B",
                    new BigDecimal("350000000"),
                    LocalDate.of(2026, 9, 15),
                    LocalDate.of(2027, 3, 31)
            );

            Employee employee1 = createEmployee(
                    "Nguyen Van An",
                    new BigDecimal("25000000"),
                    LocalDate.of(2023, 3, 10),
                    "an.nguyen@company.com",
                    Gender.MALE,
                    true
            );
            Employee employee2 = createEmployee(
                    "Tran Thi Binh",
                    new BigDecimal("22000000"),
                    LocalDate.of(2024, 1, 15),
                    "binh.tran@company.com",
                    Gender.FEMALE,
                    true
            );
            Employee employee3 = createEmployee(
                    "Le Minh Chau",
                    new BigDecimal("20000000"),
                    LocalDate.of(2025, 6, 2),
                    "chau.le@company.com",
                    Gender.OTHER,
                    false
            );

            // Phan cong cheo: NV1 -> A + B, NV2 -> B, NV3 -> A.
            employee1.assignProject(projectA);
            employee1.assignProject(projectB);
            employee2.assignProject(projectB);
            employee3.assignProject(projectA);

            entityManager.persist(projectA);
            entityManager.persist(projectB);
            entityManager.persist(employee1);
            entityManager.persist(employee2);
            entityManager.persist(employee3);

            transaction.commit();

            // Doc lai tu database de kiem tra du lieu va quan he da duoc luu.
            entityManager.clear();
            List<Long> employeeIds = List.of(
                    employee1.getId(),
                    employee2.getId(),
                    employee3.getId()
            );
            List<Employee> employees = entityManager.createQuery("""
                            SELECT DISTINCT e
                            FROM Employee e
                            LEFT JOIN FETCH e.projects
                            WHERE e.id IN :employeeIds
                            ORDER BY e.id
                            """, Employee.class)
                    .setParameter("employeeIds", employeeIds)
                    .getResultList();

            printEmployeeProjects(employees);
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalStateException("Khong the chay chuong trinh demo", exception);
        } finally {
            entityManager.close();
            JPAutil.close();
        }
    }

    private static Employee createEmployee(String fullName,
                                           BigDecimal salary,
                                           LocalDate hireDate,
                                           String email,
                                           Gender gender,
                                           boolean active) {
        Employee employee = new Employee();
        employee.setFullName(fullName);
        employee.setSalary(salary);
        employee.setHireDate(hireDate);
        employee.setEmail(email);
        employee.setGender(gender);
        employee.setActive(active);
        return employee;
    }

    private static Project createProject(String projectCode,
                                         String projectName,
                                         BigDecimal budget,
                                         LocalDate startDate,
                                         LocalDate endDate) {
        Project project = new Project();
        project.setProjectCode(projectCode);
        project.setProjectName(projectName);
        project.setBudget(budget);
        project.setStartDate(startDate);
        project.setEndDate(endDate);
        return project;
    }

    private static void printEmployeeProjects(List<Employee> employees) {
        System.out.println("\n===== DANH SACH PROJECT CUA TUNG NHAN VIEN =====");
        for (Employee employee : employees) {
            System.out.printf("%s (salary=%s, hireDate=%s, gender=%s, active=%s)%n",
                    employee.getFullName(),
                    employee.getSalary(),
                    employee.getHireDate(),
                    employee.getGender(),
                    employee.isActive());

            employee.getProjects().stream()
                    .sorted((first, second) -> first.getProjectCode()
                            .compareTo(second.getProjectCode()))
                    .forEach(project -> System.out.printf("  - %s: %s%n",
                            project.getProjectCode(),
                            project.getProjectName()));
        }
    }

}
