package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.pojo.Student;
import com.hsf302.ch4.repository.CourseRepository;
import com.hsf302.ch4.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
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

    private Course requireCourse(String courseCode) {
        String code = requireText(courseCode, "Course code");
        return courseRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + code));
    }

    private String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
