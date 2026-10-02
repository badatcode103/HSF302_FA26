package com.hsf302.ch4.repository;

import com.hsf302.ch4.dto.CourseStatDTO;
import com.hsf302.ch4.dto.CourseEnrollmentCount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT new com.hsf302.ch4.dto.CourseStatDTO(
                c.code, c.name, c.capacity, COUNT(s), AVG(s.gpa))
            FROM Course c LEFT JOIN c.students s
            GROUP BY c.id, c.code, c.name, c.capacity
            ORDER BY c.code
            """)
    List<CourseStatDTO> getStatistics();

    @Query("""
            SELECT c FROM Course c
            WHERE SIZE(c.students) >= c.capacity
            ORDER BY c.code
            """)
    List<Course> findFullCourses();

    @EntityGraph(attributePaths = "students")
    @Query("SELECT c FROM Course c WHERE c.code = :code")
    Optional<Course> findWithStudentsByCode(@Param("code") String code);

    @Query(value = """
            SELECT TOP (:n)
                   c.code AS code,
                   c.name AS name,
                   COUNT(sc.student_id) AS enrolled
            FROM courses c
            LEFT JOIN student_courses sc ON sc.course_id = c.id
            GROUP BY c.code, c.name
            ORDER BY COUNT(sc.student_id) DESC, c.code ASC
            """, nativeQuery = true)
    List<CourseEnrollmentCount> findTopEnrolled(@Param("n") int n);
}
