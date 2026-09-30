package com.hsf302.ch4.service;

import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    long count();

    Optional<Student> findById(Long id);

    List<Student> findAllOrderByGpaDesc();

    Page<Student> findPage(int pageIndex, int size, String sortField);

    // 8
    Optional<Student> findByStudentCode(String code);

    boolean isEmailExisted(String email);

    long countActive();

    // 9
    List<Student> searchByName(String kw);

    List<Student> findByEmailDomain(String domain);

    List<Student> findWithoutEmail();

    // 10
    List<Student> findByGpaRange(double min, double max);

    List<Student> findActiveByGender(Gender g);

    List<Student> findBornAfter(LocalDate d);

    // 11
    List<Student> findByDepartment(String deptCode);

    long countByDepartment(String deptCode);

    List<Student> findTop3ByGpa();

    // todo 12
    List<Student> findGoodStudents(String deptCode, double minGpa);

    // TODO 13
    List<Student> searchByKeyword(String kw);

    // TODO 15
    List<Student> findAboveAverageGpa();

    // TODO 17
    List<Student> findTopNInDepartment(String deptCode, int n);

}
