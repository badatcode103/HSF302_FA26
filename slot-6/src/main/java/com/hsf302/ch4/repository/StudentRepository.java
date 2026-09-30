package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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

}
