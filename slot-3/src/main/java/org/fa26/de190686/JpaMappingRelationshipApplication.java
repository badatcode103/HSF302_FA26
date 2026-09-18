package org.fa26.de190686;

import org.fa26.de190686.dao.DepartmentDAO;
import org.fa26.de190686.dao.daoImpl.DepartmentDAOIpml;
import org.fa26.de190686.enumPackage.Gender;
import org.fa26.de190686.pojo.Department;
import org.fa26.de190686.pojo.Employee;
import org.fa26.de190686.util.JPAutil;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class JpaMappingRelationshipApplication {

    public static void main(String[] args) {
        DepartmentDAO departmentDAO = new DepartmentDAOIpml();

        try {
            Department savedDepartment = createDemoData(departmentDAO);
            showDepartmentLoadedWithEmployees(departmentDAO, savedDepartment.getId());

            demonstrateNPlusOne(departmentDAO);
            demonstrateJoinFetch(departmentDAO);
        } catch (Exception exception) {
            System.err.println("Demo failed: " + exception.getMessage());
            exception.printStackTrace();
        } finally {
            JPAutil.closeEntityManagerFactory();
        }
    }

    private static Department createDemoData(DepartmentDAO departmentDAO) {
        // Dung ma theo thoi gian de co the chay demo nhieu lan ma khong trung
        // department.name va employee.email (hai cot UNIQUE).
        String runId = String.valueOf(System.currentTimeMillis());

        Department department = new Department("Marketing-" + runId, "Ha Noi");

        Employee employee1 = createEmployee(
                "aa.nguyen+" + runId + "@company.com",
                "Nguyen Van A",
                Gender.MALE,
                "15000000",
                LocalDate.of(2022, 1, 10)
        );
        Employee employee2 = createEmployee(
                "bb.tran+" + runId + "@company.com",
                "Tran Thi B",
                Gender.FEMALE,
                "18000000",
                LocalDate.of(2021, 6, 1)
        );
        Employee employee3 = createEmployee(
                "cc.le+" + runId + "@company.com",
                "Le Van C",
                Gender.OTHER,
                "12000000",
                LocalDate.of(2023, 3, 15)
        );

        // Dong bo ca hai phia cua quan he. CascadeType.ALL se persist Employee.
        department.addEmployee(employee1);
        department.addEmployee(employee2);
        department.addEmployee(employee3);

        departmentDAO.save(department);

        System.out.println("\n========== TODO 2.7 - CASCADE SAVE ==========");
        System.out.println("Saved Department, id = " + department.getId());
        System.out.println("Saved 3 Employees by persisting only the Department.");
        return department;
    }

    private static Employee createEmployee(
            String email,
            String fullName,
            Gender gender,
            String salary,
            LocalDate hireDate
    ) {
        return new Employee(
                email,
                fullName,
                gender,
                new BigDecimal(salary),
                hireDate
        );
    }

    private static void showDepartmentLoadedWithEmployees(
            DepartmentDAO departmentDAO,
            Long departmentId
    ) {
        System.out.println("\n========== TODO 2.6 - FIND BY ID WITH JOIN FETCH ==========");
        Department department = departmentDAO.findDepartmentById(departmentId);

        if (department == null) {
            System.out.println("Department not found, id = " + departmentId);
            return;
        }

        printDepartment(department);
    }

    private static void demonstrateNPlusOne(DepartmentDAO departmentDAO) {
        System.out.println("\n========== TODO 2.8 - N+1 QUERY ==========");
        System.out.println("Expected: 1 query for Departments + N queries for Employees.");

        List<Department> departments = departmentDAO.findAllWithNPlusOne();
        System.out.printf(
                "N = %d Department(s), expected total = 1 + %d SQL queries.%n",
                departments.size(),
                departments.size()
        );
    }

    private static void demonstrateJoinFetch(DepartmentDAO departmentDAO) {
        System.out.println("\n========== TODO 2.9 - FIX WITH JOIN FETCH ==========");
        System.out.println("Expected: only 1 query to load Departments and Employees.");

        List<Department> departments = departmentDAO.findAllWithEmployees();

        // DAO da dong EntityManager, nhung employees van truy cap duoc vi da
        // duoc khoi tao trong cung cau LEFT JOIN FETCH.
        for (Department department : departments) {
            System.out.printf(
                    "Department %d (%s) has %d employee(s).%n",
                    department.getId(),
                    department.getName(),
                    department.getEmployees().size()
            );
        }

        System.out.println("Comparison: N+1 = 1 + N queries; JOIN FETCH = 1 query.");
    }

    private static void printDepartment(Department department) {
        System.out.printf(
                "Department %d: %s - %s%n",
                department.getId(),
                department.getName(),
                department.getLocation()
        );

        for (Employee employee : department.getEmployees()) {
            System.out.printf(
                    "  Employee %d: %s | %s | %s | active=%s%n",
                    employee.getId(),
                    employee.getFullName(),
                    employee.getEmail(),
                    employee.getGender(),
                    employee.isActive()
            );
        }

    }

}
