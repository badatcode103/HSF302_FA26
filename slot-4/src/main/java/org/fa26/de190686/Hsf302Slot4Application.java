package org.fa26.de190686;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.fa26.de190686.dao.ProjectEmployeeDao;
import org.fa26.de190686.pojo.Employee;
import org.fa26.de190686.pojo.Gender;
import org.fa26.de190686.pojo.Project;
import org.fa26.de190686.util.JPAutil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Hsf302Slot4Application {

    public static void main(String[] args) {
        try {
            DemoData demoData = createDemoData();
            runProjectEmployeeDaoDemo(demoData);
        } finally {
            JPAutil.close();
        }
    }

    private static DemoData createDemoData() {
        EntityManager entityManager = JPAutil.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            // Tao ma rieng cho moi lan chay de khong trung projectCode va email.
            String runId = UUID.randomUUID().toString().substring(0, 8);

            Project projectA = createProject(
                    "DEMO-A-" + runId,
                    "Demo Project A (" + runId + ")",
                    new BigDecimal("500000000"),
                    LocalDate.of(2026, 9, 1),
                    null
            );
            Project projectB = createProject(
                    "DEMO-B-" + runId,
                    "Demo Project B (" + runId + ")",
                    new BigDecimal("350000000"),
                    LocalDate.of(2026, 9, 15),
                    LocalDate.of(2027, 3, 31)
            );

            Employee employee1 = createEmployee(
                    "Nguyen Van An",
                    new BigDecimal("25000000"),
                    LocalDate.of(2023, 3, 10),
                    "an.nguyen." + runId + "@company.com",
                    Gender.MALE,
                    true
            );
            Employee employee2 = createEmployee(
                    "Tran Thi Binh",
                    new BigDecimal("22000000"),
                    LocalDate.of(2024, 1, 15),
                    "binh.tran." + runId + "@company.com",
                    Gender.FEMALE,
                    true
            );
            Employee employee3 = createEmployee(
                    "Le Minh Chau",
                    new BigDecimal("20000000"),
                    LocalDate.of(2025, 6, 2),
                    "chau.le." + runId + "@company.com",
                    Gender.OTHER,
                    false
            );

            // Du lieu ban dau: An -> A, Binh -> B, Chau (inactive) -> A.
            // An se duoc gan them vao B bang method assignEmployeeToProject().
            employee1.assignProject(projectA);
            employee2.assignProject(projectB);
            employee3.assignProject(projectA);

            entityManager.persist(projectA);
            entityManager.persist(projectB);
            entityManager.persist(employee1);
            entityManager.persist(employee2);
            entityManager.persist(employee3);

            transaction.commit();
            return new DemoData(employee1.getId(), employee2.getId(), projectB);
        } catch (Exception exception) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new IllegalStateException("Khong the chay chuong trinh demo", exception);
        } finally {
            entityManager.close();
        }
    }

    private static void runProjectEmployeeDaoDemo(DemoData data) {
        ProjectEmployeeDao dao = new ProjectEmployeeDao();

        System.out.println("\n===== DU LIEU BAN DAU =====");
        printEmployeeState(data.employeeAnId());
        printEmployeeState(data.employeeBinhId());

        System.out.println("\n===== TODO 5.6: ASSIGN EMPLOYEE TO PROJECT =====");
        String assignResult = dao.assignEmployeeToProject(
                data.employeeAnId(),
                data.projectB().getId()
        );
        System.out.println(assignResult);
        printEmployeeState(data.employeeAnId());

        System.out.println("\n===== TODO 5.8: ACTIVE EMPLOYEE VA TONG SALARY =====");
        dao.countActiveEmployeeAndCalculateSumSalary();

        System.out.println("\n===== TODO 5.9: UNASSIGN EMPLOYEE FROM PROJECT =====");
        boolean removed = dao.unassignFromProject(data.projectB(), data.employeeBinhId());
        System.out.println("Da go Binh khoi project B: " + removed);
        printEmployeeState(data.employeeBinhId());
        printProjectState(data.projectB().getId());

        System.out.println("\n===== TODO 5.10: ACTIVE EMPLOYEE THAM GIA HON 1 PROJECT =====");
        List<Employee> employees = dao.findActiveEmployeeJoinMoreThanOneProject();
        if (employees.isEmpty()) {
            System.out.println("Khong co nhan vien phu hop.");
        } else {
            employees.forEach(employee -> System.out.printf(
                    "- id=%d, name=%s, email=%s%n",
                    employee.getId(),
                    employee.getFullName(),
                    employee.getEmail()
            ));
        }

        System.out.println("\n===== TODO 5.11: DEACTIVATE EMPLOYEE =====");
        dao.deactivateEmployee(data.employeeAnId());
        System.out.println("Da deactivate Nguyen Van An; cac lien ket project van duoc giu lai:");
        printEmployeeState(data.employeeAnId());
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

    private static void printEmployeeState(Long employeeId) {
        EntityManager em = JPAutil.getEntityManager();
        try {
            Employee employee = em.createQuery("""
                            SELECT DISTINCT e
                            FROM Employee e
                            LEFT JOIN FETCH e.projects
                            WHERE e.id = :employeeId
                            """, Employee.class)
                    .setParameter("employeeId", employeeId)
                    .getSingleResult();

            System.out.printf("%s (id=%d, active=%s, projects=%d)%n",
                    employee.getFullName(),
                    employee.getId(),
                    employee.isActive(),
                    employee.getProjects().size());
            employee.getProjects().stream()
                    .sorted((first, second) -> first.getProjectCode().compareTo(second.getProjectCode()))
                    .forEach(project -> System.out.printf(
                            "  - %s: %s%n",
                            project.getProjectCode(),
                            project.getProjectName()
                    ));
        } finally {
            em.close();
        }
    }

    private static void printProjectState(Long projectId) {
        EntityManager em = JPAutil.getEntityManager();
        try {
            Project project = em.createQuery("""
                            SELECT DISTINCT p
                            FROM Project p
                            LEFT JOIN FETCH p.employees
                            WHERE p.id = :projectId
                            """, Project.class)
                    .setParameter("projectId", projectId)
                    .getSingleResult();

            System.out.printf("Project van ton tai: %s (employees=%d)%n",
                    project.getProjectCode(),
                    project.getEmployees().size());
        } finally {
            em.close();
        }
    }

    private record DemoData(
            Long employeeAnId,
            Long employeeBinhId,
            Project projectB
    ) {
    }
}
