package com.hsf302.ch4.runner;

import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Department;
import com.hsf302.ch4.pojo.Student;
import com.hsf302.ch4.service.DepartmentService;
import com.hsf302.ch4.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.hibernate.LazyInitializationException;

import java.time.LocalDate;

@Component
@Order(2)
@RequiredArgsConstructor
public class ExerciseRunner implements CommandLineRunner {

    private final DepartmentService departmentService;
    private final StudentService studentService;

    @Override
    public void run(String... args) {
        runTodo6();
        runTodo7();
        runTodo8();
        runTodo9();
        runTodo10();
        runTodo11();
        runTodo12();
        runTodo13();
        runTodo14();
        runTodo15();
        runTodo16();
        runTodo17();
        runTodo18();
    }

    private void runTodo6() {
        System.out.println("===== TODO 6: JpaRepository built-in methods =====");
        System.out.printf("%d departments, %d students%n", departmentService.count(), studentService.count());
        System.out.println("Student id=1: " + studentService.findById(1L).orElse(null));
        System.out.println("Student id=99: " + studentService.findById(99L)
                .map(Object::toString)
                .orElse("Not found"));
        System.out.println("Department id=4 exists: " + departmentService.existsById(4L));
    }

    private void runTodo7() {
        System.out.println("===== TODO 7: Sort and Pageable =====");
        System.out.println("Students ordered by GPA descending:");
        studentService.findAllOrderByGpaDesc().forEach(System.out::println);

        Page<Student> page = studentService.findPage(1, 3, "fullName");
        System.out.println("Page 2 ordered by full name:");
        page.getContent().forEach(System.out::println);
        System.out.println("totalElements = " + page.getTotalElements());
        System.out.println("totalPages = " + page.getTotalPages());
        System.out.println("hasNext = " + page.hasNext());
    }

    private void runTodo8() {
        System.out.println("===== TODO 8: Basic derived queries =====");
        System.out.println("Student AI002: " + studentService.findByStudentCode("AI002")
                .map(Object::toString)
                .orElse("Not found"));
        System.out.println("Student XX999: " + studentService.findByStudentCode("XX999")
                .map(Object::toString)
                .orElse("Not found"));
        System.out.println("Email binh.tt@fpt.edu.vn exists: "
                + studentService.isEmailExisted("binh.tt@fpt.edu.vn"));
        System.out.println("Active students: " + studentService.countActive());
    }

    private void runTodo9() {
        System.out.println("===== TODO 9: String and null derived queries =====");
        System.out.println("Students whose name contains 'nguyen':");
        studentService.searchByName("nguyen").forEach(System.out::println);

        System.out.println("Students with @gmail.com email:");
        studentService.findByEmailDomain("gmail.com").forEach(System.out::println);

        System.out.println("Students without email:");
        studentService.findWithoutEmail().forEach(System.out::println);
    }

    private void runTodo10() {
        System.out.println("===== TODO 10: Range, boolean and date derived queries =====");
        System.out.println("Students with GPA from 3.0 to 3.6:");
        studentService.findByGpaRange(3.0, 3.6).forEach(System.out::println);

        System.out.println("Active male students:");
        studentService.findActiveByGender(Gender.MALE).forEach(System.out::println);

        System.out.println("Students born after 2005-01-01:");
        studentService.findBornAfter(LocalDate.of(2005, 1, 1)).forEach(System.out::println);
    }

    private void runTodo11() {
        System.out.println("===== TODO 11: Nested, top and empty derived queries =====");
        System.out.println("Students in department SE:");
        studentService.findByDepartment("SE").forEach(System.out::println);

        System.out.println("Students in department AI: " + studentService.countByDepartment("AI"));

        System.out.println("Top 3 students by GPA:");
        studentService.findTop3ByGpa().forEach(System.out::println);

        System.out.println("Departments without students:");
        departmentService.findDepartmentsWithoutStudents()
                .forEach(department -> System.out.println(
                        department.getCode() + " - " + department.getName()));
    }

     private void runTodo12() {
        System.out.println("===== TODO 12: Custom query with @Query =====");
        System.out.println("Good students in department SE:");
        studentService.findGoodStudents("SE", 3.0).forEach(System.out::println);
    }

    private void runTodo13() {
        System.out.println("===== TODO 13: Custom query with @Query and keyword search =====");
        System.out.println("Students with keyword 'nguyen':");
        studentService.searchByKeyword("nguyen").forEach(System.out::println);

        System.out.println("Students with keyword 'gmail':");
        studentService.searchByKeyword("gmail").forEach(System.out::println);
    }

    private void runTodo14() {
    System.out.println("===== TODO 14: Department statistics =====");

    departmentService.findDepartmentStats().forEach(stat -> {
        String averageGpa = stat.averageGpa() == null
                ? "null"
                : String.format("%.3f", stat.averageGpa());

        System.out.printf(
                "%s - %s: %d - %s%n",
                stat.departmentCode(),
                stat.departmentName(),
                stat.studentCount(),
                averageGpa
        );
    });
}

    private void runTodo15() {
        System.out.println("===== TODO 15: Students above average GPA =====");
        studentService.findAboveAverageGpa().forEach(System.out::println);
    }

    private void runTodo16() {
        System.out.println("===== TODO 16: Lazy loading and JOIN FETCH =====");
        Department lazyDepartment = departmentService.findByCode("AI").orElseThrow();
        try {
            System.out.println("AI student count: " + lazyDepartment.getStudents().size());
        } catch (LazyInitializationException exception) {
            System.out.println("LazyInitializationException: " + exception.getMessage());
        }

        Department departmentWithStudents = departmentService.getWithStudents("AI");
        System.out.println(departmentWithStudents.getCode() + " - " + departmentWithStudents.getName());
        departmentWithStudents.getStudents().forEach(System.out::println);
    }

    private void runTodo17() {
        System.out.println("===== TODO 17: Top students in department with native SQL =====");
        studentService.findTopNInDepartment("SE", 2).forEach(System.out::println);
    }

    private void runTodo18() {
        System.out.println("===== TODO 18: Active student summaries =====");
        studentService.getActiveSummaries().forEach(summary -> System.out.printf(
                "%s - %s - %.2f - %s%n",
                summary.getStudentCode(),
                summary.getFullName(),
                summary.getGpa(),
                summary.getDepartmentName()));
    }


}
