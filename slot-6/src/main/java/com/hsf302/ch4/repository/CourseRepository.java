package com.hsf302.ch4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsf302.ch4.pojo.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);

    List<Course> findBySemesterOrderByCodeAsc(String semester);

    long countBySemester(String semester);

    List<Course> findByStudents_StudentCodeOrderByCodeAsc(String studentCode);

    List<Course> findByStudents_Department_CodeOrderByCodeAsc(String departmentCode);

    List<Course> findDistinctByStudents_Department_CodeOrderByCodeAsc(String departmentCode);

    List<Course> findByStudentsIsEmptyOrderByCodeAsc();
}
