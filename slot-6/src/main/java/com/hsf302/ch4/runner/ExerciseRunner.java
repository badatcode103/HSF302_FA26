package com.hsf302.ch4.runner;

import com.hsf302.ch4.service.DepartmentService;
import com.hsf302.ch4.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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

        Page<com.hsf302.ch4.pojo.Student> page = studentService.findPage(1, 3, "fullName");
        System.out.println("Page 2 ordered by full name:");
        page.getContent().forEach(System.out::println);
        System.out.println("totalElements = " + page.getTotalElements());
        System.out.println("totalPages = " + page.getTotalPages());
        System.out.println("hasNext = " + page.hasNext());
    }
}
