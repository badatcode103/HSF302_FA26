package com.hsf302.ch4.runner;

import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.pojo.Student;
import com.hsf302.ch4.service.CourseService;
import com.hsf302.ch4.service.EnrollmentService;
import com.hsf302.ch4.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Locale;

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
        runTodo11();
        runTodo12();
        runTodo13();
        runTodo14();
        runTodo15();
        runTodo16();
        runTodo17();
        runTodo18();
        runTodo19();
        runTodo20();
        runTodo21();
        runTodo22();
        runTodo23();
        runTodo24();
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

    private void runTodo11() {
        heading(11, "Find unenrolled students and empty courses");
        System.out.println("Students without courses:");
        enrollmentService.findStudentsWithoutCourses().forEach(System.out::println);
        System.out.println("Courses without students:");
        courseService.findCoursesWithoutStudents().forEach(System.out::println);
        System.out.println("SE001 enrolled in AIL303: "
                + enrollmentService.isEnrolled("SE001", "AIL303"));
        System.out.println("SE002 enrolled in AIL303: "
                + enrollmentService.isEnrolled("SE002", "AIL303"));
    }

    private void runTodo12() {
        heading(12, "Find good students in a course");
        enrollmentService.findGoodStudentsInCourse("HSF302", 3.5)
                .forEach(System.out::println);
    }

    private void runTodo13() {
        heading(13, "Course enrollment statistics");
        courseService.getStatistics().forEach(stat -> {
            String average = stat.averageGpa() == null
                    ? "null"
                    : String.format(Locale.US, "%.3f", stat.averageGpa());
            System.out.printf("%s | %s | %d/%d | remaining %d | avg GPA %s%n",
                    stat.code(), stat.name(), stat.enrolled(), stat.capacity(),
                    stat.remaining(), average);
        });
    }

    private void runTodo14() {
        heading(14, "Summarize student credits");
        enrollmentService.getCreditSummary(7).forEach(summary ->
                System.out.printf("%s | %s | %d courses | %d credits%n",
                        summary.studentCode(), summary.fullName(),
                        summary.courseCount(), summary.totalCredits()));
    }

    private void runTodo15() {
        heading(15, "Find full courses and busy students");
        System.out.println("Full courses:");
        courseService.findFullCourses().forEach(System.out::println);
        System.out.println("Students enrolled in more than 2 courses:");
        enrollmentService.findStudentsWithMoreThan(2).forEach(System.out::println);
    }

    private void runTodo16() {
        heading(16, "Load lazy collections explicitly");
        try {
            Student detached = studentService.findByStudentCode("SE001").orElseThrow();
            System.out.println("Detached course count: " + detached.getCourses().size());
        } catch (RuntimeException exception) {
            System.out.println("Expected lazy-loading failure: "
                    + exception.getClass().getSimpleName() + " - " + exception.getMessage());
        }

        Student student = enrollmentService.getStudentWithCourses("SE001");
        System.out.println("SE001 courses loaded with JOIN FETCH:");
        student.getCourses().stream()
                .sorted(Comparator.comparing(Course::getCode))
                .forEach(System.out::println);

        Course course = courseService.getWithStudents("SWP391");
        System.out.println("SWP391 students loaded with EntityGraph:");
        course.getStudents().stream()
                .sorted(Comparator.comparing(Student::getFullName))
                .forEach(System.out::println);
    }

    private void runTodo17() {
        heading(17, "Top enrolled courses");
        courseService.findTopEnrolled(3).forEach(course ->
                System.out.printf("%s | %s | %d students%n",
                        course.getCode(), course.getName(), course.getEnrolled()));
    }

    private void runTodo18() {
        heading(18, "Enrollment view by department");
        enrollmentService.getEnrollmentsOfDepartment("AI").forEach(enrollment ->
                System.out.printf("%s | %s | %s | %s | %d credits%n",
                        enrollment.getStudentCode(), enrollment.getFullName(),
                        enrollment.getCourseCode(), enrollment.getCourseName(),
                        enrollment.getCredits()));
    }

    private void runTodo19() {
        heading(19, "Paginate students of a course");
        int pageIndex = 0;
        var page = enrollmentService.findStudentsInCoursePage("HSF302", pageIndex, 2);
        System.out.printf("totalElements=%d, totalPages=%d%n",
                page.getTotalElements(), page.getTotalPages());
        do {
            System.out.println("Page " + pageIndex + ":");
            page.getContent().forEach(System.out::println);
            pageIndex++;
            if (pageIndex < page.getTotalPages()) {
                page = enrollmentService.findStudentsInCoursePage("HSF302", pageIndex, 2);
            }
        } while (pageIndex < page.getTotalPages());
    }

    private void runTodo20() {
        heading(20, "Enroll a student with business rules");
        attempt("IA003 -> MKT101", () -> enrollmentService.enroll("IA003", "MKT101"));
        attempt("SE001 -> PRJ301", () -> enrollmentService.enroll("SE001", "PRJ301"));
        attempt("SE004 -> AIL303", () -> enrollmentService.enroll("SE004", "AIL303"));
        attempt("SE003 -> HSF302", () -> enrollmentService.enroll("SE003", "HSF302"));
        attempt("XX999 -> HSF302", () -> enrollmentService.enroll("XX999", "HSF302"));
        System.out.println("Courses of IA003:");
        enrollmentService.getCoursesOfStudent("IA003").forEach(System.out::println);
        System.out.println("Students in MKT101: "
                + enrollmentService.countStudentsInCourse("MKT101"));
    }

    private void runTodo21() {
        heading(21, "Unenroll a student from a course");
        attempt("AI002 leaves AIL303", () -> enrollmentService.unenroll("AI002", "AIL303"));
        attempt("IA003 leaves PRJ301", () -> enrollmentService.unenroll("IA003", "PRJ301"));
        attempt("SE004 -> AIL303", () -> enrollmentService.enroll("SE004", "AIL303"));
        System.out.println("Students of AIL303:");
        enrollmentService.getStudentsOfCourse("AIL303").forEach(System.out::println);
        System.out.println("Courses of AI002:");
        enrollmentService.getCoursesOfStudent("AI002").forEach(System.out::println);
        System.out.println("AI002 still exists: "
                + studentService.findByStudentCode("AI002").isPresent());
        System.out.println("Total courses: " + courseService.count());
    }

    private void runTodo22() {
        heading(22, "Switch courses atomically");
        attempt("SE001 SWP391 -> MKT101",
                () -> enrollmentService.switchCourse("SE001", "SWP391", "MKT101"));
        printCoursesOfStudent("SE001");
        attempt("SE001 PRJ301 -> AIL303",
                () -> enrollmentService.switchCourse("SE001", "PRJ301", "AIL303"));
        printCoursesOfStudent("SE001");
    }

    private void runTodo23() {
        heading(23, "Delete a course safely");
        try {
            courseService.deleteCourseDirectly("IAA202");
            System.out.println("Direct deletion unexpectedly succeeded");
        } catch (RuntimeException exception) {
            System.out.println("Expected direct deletion failure: "
                    + exception.getClass().getSimpleName() + " - " + exception.getMessage());
        }
        int removed = courseService.deleteCourse("IAA202");
        System.out.println("Students detached from IAA202: " + removed);
        System.out.println("Remaining courses:");
        courseService.findAllOrderByCode().forEach(System.out::println);
        printCoursesOfStudent("IA002");
    }

    private void runTodo24() {
        heading(24, "Remove enrollments of inactive students");
        int removed = enrollmentService.removeEnrollmentsOfInactiveStudents();
        System.out.println("Enrollment rows removed: " + removed);
        System.out.println("Updated course statistics:");
        courseService.getStatistics().forEach(stat -> {
            String average = stat.averageGpa() == null
                    ? "null"
                    : String.format(Locale.US, "%.3f", stat.averageGpa());
            System.out.printf("%s | %d/%d | remaining %d | avg GPA %s%n",
                    stat.code(), stat.enrolled(), stat.capacity(), stat.remaining(), average);
        });
        System.out.println("Students without courses:");
        enrollmentService.findStudentsWithoutCourses().forEach(System.out::println);
    }

    private void printCoursesOfStudent(String studentCode) {
        System.out.println("Courses of " + studentCode + ":");
        enrollmentService.getCoursesOfStudent(studentCode).forEach(System.out::println);
    }

    private void attempt(String action, Runnable operation) {
        try {
            operation.run();
            System.out.println(action + ": success");
        } catch (RuntimeException exception) {
            System.out.println(action + ": failed - " + exception.getMessage());
        }
    }

    private void heading(int todo, String title) {
        System.out.printf("%n===== TODO %d: %s =====%n", todo, title);
    }
}
