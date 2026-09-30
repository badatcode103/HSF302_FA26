package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    //8a
    Optional<Student> findByStudentCode(String studentCode);

    //8b
    boolean existsByEmail(String email);

    //8c
    long countByActiveTrue();

    //9a
    List<Student> findByFullNameContainingIgnoreCase(String fullName);

    //9b
    List<Student> findByEmailEndingWith(String email);

    //9c
    List<Student> findByEmailIsNull();

    //10a
    List<Student> findByGpaBetweenOrderByGpaDesc(Double minGpa, Double maxGpa);

    //10b
    List<Student> findByGenderAndActiveTrue(Gender gender);

    //10c
    List<Student> findByDobAfter(LocalDate dob);

    //11a
    List<Student> findByDepartment_CodeOrderByFullNameAsc(String departmentCode);

    //11b
    long countByDepartment_Code(String departmentCode);

    //11c
    List<Student> findTop3ByOrderByGpaDesc();

    //todo 12
    @Query("""
            SELECT s FROM Student s
            WHERE s.department.code = :deptCode AND s.gpa >= :minGpa
            ORDER BY s.gpa DESC
            """)
    List<Student> findGoodStudents(@Param("deptCode") String deptCode, @Param("minGpa") double minGpa);

    //todo 13
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

}
