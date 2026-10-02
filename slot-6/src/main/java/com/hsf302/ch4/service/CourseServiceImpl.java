package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.CourseStatDTO;
import com.hsf302.ch4.pojo.Course;
import com.hsf302.ch4.repository.CourseRepository;
import com.hsf302.ch4.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    @Override
    public long count() {
        return courseRepository.count();
    }

    @Override
    public List<Course> findAllOrderByCode() {
        return courseRepository.findAll(Sort.by(Sort.Direction.ASC, "code"));
    }

    @Override
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Optional<Course> findByCode(String code) {
        return courseRepository.findByCode(requireText(code, "Course code"));
    }

    @Override
    public List<Course> findBySemester(String semester) {
        return courseRepository.findBySemesterOrderByCodeAsc(requireText(semester, "Semester"));
    }

    @Override
    public long countBySemester(String semester) {
        return courseRepository.countBySemester(requireText(semester, "Semester"));
    }

    @Override
    public List<Course> findCoursesOfStudent(String studentCode) {
        String code = requireText(studentCode, "Student code");
        if (!studentRepository.findByStudentCode(code).isPresent()) {
            throw new IllegalArgumentException("Student not found: " + code);
        }
        return courseRepository.findByStudents_StudentCodeOrderByCodeAsc(code);
    }

    @Override
    public List<Course> findCoursesOfDepartment(String deptCode, boolean distinct) {
        String code = requireText(deptCode, "Department code");
        if (studentRepository.countByDepartment_Code(code) == 0) {
            throw new IllegalArgumentException("Department not found or has no students: " + code);
        }
        return distinct
                ? courseRepository.findDistinctByStudents_Department_CodeOrderByCodeAsc(code)
                : courseRepository.findByStudents_Department_CodeOrderByCodeAsc(code);
    }

    @Override
    public List<Course> findCoursesWithoutStudents() {
        return courseRepository.findByStudentsIsEmptyOrderByCodeAsc();
    }

    @Override
    public List<CourseStatDTO> getStatistics() {
        return courseRepository.getStatistics();
    }

    @Override
    public List<Course> findFullCourses() {
        return courseRepository.findFullCourses();
    }

    private String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }
}
