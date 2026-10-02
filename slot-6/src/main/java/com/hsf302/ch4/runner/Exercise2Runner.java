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
        runTodo7();
        runTodo8();
        runTodo9();
        runTodo10();
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

    private void runTodo7() {
        heading(7, "Navigate the many-to-many relationship");
        System.out.println("Courses of SE001:");
        enrollmentService.getCoursesOfStudent("SE001").forEach(System.out::println);
        System.out.println("Students of AIL303:");
        enrollmentService.getStudentsOfCourse("AIL303").forEach(System.out::println);
    }

    private void runTodo8() {
        heading(8, "Find courses by code and semester");
        System.out.println("HSF302: " + courseService.findByCode("HSF302")
                .map(Object::toString).orElse("Not found"));
        System.out.println("XXX000: " + courseService.findByCode("XXX000")
                .map(Object::toString).orElse("Not found"));
        System.out.println("Courses in SU26:");
        courseService.findBySemester("SU26").forEach(System.out::println);
        System.out.println("Courses in FA26: " + courseService.countBySemester("FA26"));
    }

    private void runTodo9() {
        heading(9, "Find students by enrolled course code");
        System.out.println("Students in PRJ301:");
        enrollmentService.findStudentsInCourse("PRJ301").forEach(System.out::println);
        System.out.println("Students in HSF302: "
                + enrollmentService.countStudentsInCourse("HSF302"));
        System.out.println("Active students in PRJ301:");
        enrollmentService.findActiveStudentsInCourse("PRJ301").forEach(System.out::println);
    }

    private void runTodo10() {
        heading(10, "Find courses by student and department");
        System.out.println("Courses of SE002:");
        courseService.findCoursesOfStudent("SE002").forEach(System.out::println);
        var coursesWithDuplicates = courseService.findCoursesOfDepartment("AI", false);
        var distinctCourses = courseService.findCoursesOfDepartment("AI", true);
        System.out.println("AI course rows without distinct: " + coursesWithDuplicates.size());
        coursesWithDuplicates.forEach(System.out::println);
        System.out.println("AI course rows with distinct: " + distinctCourses.size());
        distinctCourses.forEach(System.out::println);
    }

    private void heading(int todo, String title) {
        System.out.printf("%n===== TODO %d: %s =====%n", todo, title);
    }
}
