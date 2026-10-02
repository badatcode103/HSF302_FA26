package com.hsf302.ch4.runner;

import com.hsf302.ch4.service.CourseService;
import com.hsf302.ch4.service.EnrollmentService;
import com.hsf302.ch4.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@Profile("ex2")
@RequiredArgsConstructor
public class Exercise2Runner implements CommandLineRunner {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    @Override
    public void run(String... args) {
        runTodo6();
    }

    private void runTodo6() {
        heading(6, "Count, sort and find courses by id");
        System.out.println("Total courses: " + courseService.count());
        courseService.findAllOrderByCode().forEach(System.out::println);
        printCourseById(2L);
        printCourseById(99L);
    }

    private void printCourseById(Long id) {
        System.out.printf("Course id=%d: %s%n", id,
                courseService.findById(id).map(Object::toString).orElse("Not found"));
    }

    private void heading(int todo, String title) {
        System.out.printf("%n===== TODO %d: %s =====%n", todo, title);
    }
}
