package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.StudentCreditDTO;
import com.hsf302.ch4.dto.EnrollmentView;
import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.pojo.Student;
import com.hsf302.ch4.repository.CourseRepository;
import com.hsf302.ch4.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<Course> getCoursesOfStudent(String studentCode) {
        Student student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentCode));
        return student.getCourses().stream()
                .sorted(Comparator.comparing(Course::getCode))
                .toList();
    }

    @Override
    public List<Student> getStudentsOfCourse(String courseCode) {
        Course course = courseRepository.findByCode(requireText(courseCode, "Course code"))
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseCode));
        return course.getStudents().stream()
                .sorted(Comparator.comparing(Student::getFullName))
                .toList();
    }

    @Override
    public List<Student> findStudentsInCourse(String courseCode) {
        requireCourse(courseCode);
        return studentRepository.findByCourses_CodeOrderByFullNameAsc(courseCode);
    }

    @Override
    public long countStudentsInCourse(String courseCode) {
        requireCourse(courseCode);
        return studentRepository.countByCourses_Code(courseCode);
    }

    @Override
    public List<Student> findActiveStudentsInCourse(String courseCode) {
        requireCourse(courseCode);
        return studentRepository.findByCourses_CodeAndActiveTrueOrderByFullNameAsc(courseCode);
    }

    @Override
    public List<Student> findStudentsWithoutCourses() {
        return studentRepository.findByCoursesIsEmptyOrderByFullNameAsc();
    }

    @Override
    public boolean isEnrolled(String studentCode, String courseCode) {
        String student = requireText(studentCode, "Student code");
        if (studentRepository.findByStudentCode(student).isEmpty()) {
            throw new IllegalArgumentException("Student not found: " + student);
        }
        Course course = requireCourse(courseCode);
        return studentRepository.existsByStudentCodeAndCourses_Code(student, course.getCode());
    }

    @Override
    public List<Student> findGoodStudentsInCourse(String courseCode, double minGpa) {
        if (!Double.isFinite(minGpa) || minGpa < 0 || minGpa > 4) {
            throw new IllegalArgumentException("Minimum GPA must be between 0 and 4");
        }
        Course course = requireCourse(courseCode);
        return studentRepository.findGoodStudentsInCourse(course.getCode(), minGpa);
    }

    @Override
    public List<StudentCreditDTO> getCreditSummary(int minCredits) {
        if (minCredits < 0) {
            throw new IllegalArgumentException("Minimum credits must not be negative");
        }
        return studentRepository.getCreditSummary(minCredits);
    }

    @Override
    public List<Student> findStudentsWithMoreThan(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Number of courses must not be negative");
        }
        return studentRepository.findStudentsWithMoreThan(n);
    }

    @Override
    public Student getStudentWithCourses(String studentCode) {
        String code = requireText(studentCode, "Student code");
        return studentRepository.findWithCoursesByStudentCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + code));
    }

    @Override
    public List<EnrollmentView> getEnrollmentsOfDepartment(String deptCode) {
        String code = requireText(deptCode, "Department code");
        if (studentRepository.countByDepartment_Code(code) == 0) {
            throw new IllegalArgumentException("Department not found or has no students: " + code);
        }
        return studentRepository.getEnrollmentsOfDepartment(code);
    }

    @Override
    public Page<Student> findStudentsInCoursePage(String courseCode, int pageIndex, int size) {
        if (pageIndex < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        Course course = requireCourse(courseCode);
        return studentRepository.findStudentsInCoursePage(
                course.getCode(), PageRequest.of(pageIndex, size));
    }

    @Override
    @Transactional
    public void enroll(String studentCode, String courseCode) {
        Student student = requireStudent(studentCode);
        Course course = requireCourse(courseCode);
        if (!student.isActive()) {
            throw new IllegalStateException("Student is inactive: " + student.getStudentCode());
        }
        if (student.getCourses().contains(course)) {
            throw new IllegalStateException("Student is already enrolled in " + course.getCode());
        }
        if (course.getStudents().size() >= course.getCapacity()) {
            throw new IllegalStateException("Course is full: " + course.getCode());
        }
        student.enroll(course);
    }

    @Override
    @Transactional
    public void unenroll(String studentCode, String courseCode) {
        Student student = requireStudent(studentCode);
        Course course = requireCourse(courseCode);
        if (!student.getCourses().contains(course)) {
            throw new IllegalStateException(
                    "Student is not enrolled in " + course.getCode());
        }
        student.unenroll(course);
    }

    private Course requireCourse(String courseCode) {
        String code = requireText(courseCode, "Course code");
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));
    }

    private Student requireStudent(String studentCode) {
        String code = requireText(studentCode, "Student code");
        return studentRepository.findByStudentCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + code));
    }

    private String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
