package com.hsf302.ch4.repository;

import com.hsf302.ch4.dto.StudentSummary;
import com.hsf302.ch4.dto.StudentCreditDTO;
import com.hsf302.ch4.dto.EnrollmentView;
import com.hsf302.ch4.pojo.Department;
import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
        // 8a
        Optional<Student> findByStudentCode(String studentCode);

        // 8b
        boolean existsByEmail(String email);

        // 8c
        long countByActiveTrue();

        // 9a
        List<Student> findByFullNameContainingIgnoreCase(String fullName);

        // 9b
        List<Student> findByEmailEndingWith(String email);

        // 9c
        List<Student> findByEmailIsNull();

        // 10a
        List<Student> findByGpaBetweenOrderByGpaDesc(Double minGpa, Double maxGpa);

        // 10b
        List<Student> findByGenderAndActiveTrue(Gender gender);

        // 10c
        List<Student> findByDobAfter(LocalDate dob);

        // 11a
        List<Student> findByDepartment_CodeOrderByFullNameAsc(String departmentCode);

        // 11b
        long countByDepartment_Code(String departmentCode);

        // 11c
        List<Student> findTop3ByOrderByGpaDesc();

        // todo 12
        @Query("""
                        SELECT s FROM Student s
                        WHERE s.department.code = :deptCode AND s.gpa >= :minGpa
                        ORDER BY s.gpa DESC
                        """)
        List<Student> findGoodStudents(@Param("deptCode") String deptCode, @Param("minGpa") double minGpa);

        // todo 13
        @Query("""
                        SELECT s FROM Student s
                        WHERE LOWER(s.fullName) LIKE LOWER(CONCAT('%', :kw, '%'))
                           OR LOWER(COALESCE(s.email, '')) LIKE LOWER(CONCAT('%', :kw, '%'))
                        ORDER BY s.fullName ASC
                        """)
        List<Student> searchStudents(@Param("kw") String keyword);

        // TODO 15
        @Query("""
                        SELECT s FROM Student s
                        WHERE s.gpa > (SELECT AVG(student.gpa) FROM Student student)
                        ORDER BY s.gpa DESC
                        """)
        List<Student> findAboveAverageGpa();

        // TODO 17
        @Query(value = """
                        SELECT TOP (:n) s.*
                        FROM students s
                        JOIN departments d ON s.department_id = d.id
                        WHERE d.code = :deptCode
                        ORDER BY s.gpa DESC
                        """, nativeQuery = true)
        List<Student> findTopNInDepartment(@Param("deptCode") String deptCode, @Param("n") int n);

        // TODO 18
        @Query("""
                        SELECT s.studentCode AS studentCode,
                               s.fullName AS fullName,
                               s.gpa AS gpa,
                               s.department.name AS departmentName
                        FROM Student s
                        WHERE s.active = true
                        ORDER BY s.fullName ASC
                        """)
        List<StudentSummary> findActiveSummaries();

        // TODO 19
        @Query("""
                        SELECT s FROM Student s
                        WHERE s.active = true AND s.department.code = :deptCode
                        ORDER BY s.gpa DESC
                        """)
        Page<Student> findActiveByDepartment(@Param("deptCode") String deptCode, Pageable pageable);

        // Todo 21
        @Modifying(clearAutomatically = true)
        @Query("""
                        UPDATE Student s
                        SET s.active = false
                        WHERE s.active = true AND s.gpa < :threshold
                        """)
        int deactivateStudentsBelowGpa(@Param("threshold") double threshold);

        // Todo 22
        @Modifying(clearAutomatically = true)
        @Query("""
                        UPDATE Student s
                        SET s.department = :newDepartment
                        WHERE s.department = :oldDepartment
                        """)
        int transferStudentsToNewDepartment(@Param("oldDepartment") Department oldDepartment, @Param("newDepartment") Department newDepartment);

        // TODO 23
        long deleteByActiveFalse();

        List<Student> findByCourses_CodeOrderByFullNameAsc(String courseCode);

        long countByCourses_Code(String courseCode);

        List<Student> findByCourses_CodeAndActiveTrueOrderByFullNameAsc(String courseCode);

        List<Student> findByCoursesIsEmptyOrderByFullNameAsc();

        boolean existsByStudentCodeAndCourses_Code(String studentCode, String courseCode);

        @Query("""
                        SELECT s
                        FROM Student s JOIN s.courses c
                        WHERE c.code = :courseCode AND s.gpa >= :minGpa
                        ORDER BY s.gpa DESC
                        """)
        List<Student> findGoodStudentsInCourse(@Param("courseCode") String courseCode,
                                               @Param("minGpa") double minGpa);

        @Query("""
                        SELECT new com.hsf302.ch4.dto.StudentCreditDTO(
                            s.studentCode, s.fullName, COUNT(c), SUM(c.credits))
                        FROM Student s JOIN s.courses c
                        GROUP BY s.id, s.studentCode, s.fullName
                        HAVING SUM(c.credits) >= :minCredits
                        ORDER BY SUM(c.credits) DESC, s.fullName ASC
                        """)
        List<StudentCreditDTO> getCreditSummary(@Param("minCredits") int minCredits);

        @Query("""
                        SELECT s FROM Student s
                        WHERE SIZE(s.courses) > :numberOfCourses
                        ORDER BY s.fullName
                        """)
        List<Student> findStudentsWithMoreThan(@Param("numberOfCourses") int numberOfCourses);

        @Query("""
                        SELECT DISTINCT s FROM Student s
                        LEFT JOIN FETCH s.courses
                        WHERE s.studentCode = :studentCode
                        """)
        Optional<Student> findWithCoursesByStudentCode(@Param("studentCode") String studentCode);

        @Query("""
                        SELECT s.studentCode AS studentCode,
                               s.fullName AS fullName,
                               c.code AS courseCode,
                               c.name AS courseName,
                               c.credits AS credits
                        FROM Student s JOIN s.courses c
                        WHERE s.department.code = :deptCode
                        ORDER BY s.studentCode, c.code
                        """)
        List<EnrollmentView> getEnrollmentsOfDepartment(@Param("deptCode") String deptCode);
}
