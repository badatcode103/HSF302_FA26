package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.StudentSummary;
import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import com.hsf302.ch4.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public long count() {
        return studentRepository.count();
    }

    @Override
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> findAllOrderByGpaDesc() {
        return studentRepository.findAll(Sort.by(Sort.Direction.DESC, "gpa"));
    }

    @Override
    public Page<Student> findPage(int pageIndex, int size, String sortField) {
        if (pageIndex < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        if (sortField == null || sortField.isBlank()) {
            throw new IllegalArgumentException("Sort field must not be blank");
        }
        return studentRepository.findAll(PageRequest.of(pageIndex, size, Sort.by(sortField).ascending()));
    }


    //todo 8
    @Override
    public Optional<Student> findByStudentCode(String code) {
        return studentRepository.findByStudentCode(code);
    }

    @Override
    public boolean isEmailExisted(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public long countActive() {
        return studentRepository.countByActiveTrue();
    }

    //todo 9
    @Override
    public List<Student> searchByName(String kw) {
        if (kw == null || kw.isBlank()) {
            return List.of();
        }
        return studentRepository.findByFullNameContainingIgnoreCase(kw);
    }

    @Override
    public List<Student> findByEmailDomain(String domain) {
        if (domain == null || domain.isBlank()) {
            return List.of();
        }
        String suffix = domain.startsWith("@") ? domain : "@" + domain;
        return studentRepository.findByEmailEndingWith(suffix);
    }

    @Override
    public List<Student> findWithoutEmail() {
        return studentRepository.findByEmailIsNull();
    }

    //todo 10
    @Override
    public List<Student> findByGpaRange(double min, double max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum GPA must not be greater than maximum GPA");
        }
        return studentRepository.findByGpaBetweenOrderByGpaDesc(min, max);
    }

    @Override
    public List<Student> findActiveByGender(Gender g) {
        return studentRepository.findByGenderAndActiveTrue(g);
    }

    @Override
    public List<Student> findBornAfter(LocalDate d) {
        return studentRepository.findByDobAfter(d);
    }

    //todo 11
    @Override
    public List<Student> findByDepartment(String deptCode) {
        return studentRepository.findByDepartment_CodeOrderByFullNameAsc(deptCode);
    }

    @Override
    public long countByDepartment(String deptCode) {
        return studentRepository.countByDepartment_Code(deptCode);
    }

    @Override
    public List<Student> findTop3ByGpa() {
        return studentRepository.findTop3ByOrderByGpaDesc();
    }
    

    //todo 12
    @Override
    public List<Student> findGoodStudents(String deptCode, double minGpa) {
        return studentRepository.findGoodStudents(deptCode, minGpa);
    }
    
    //todo 13
    @Override
    public List<Student> searchByKeyword(String kw) {
        if (kw == null || kw.isBlank()) {
            return List.of();
        }
        return studentRepository.searchStudents(kw);
    }

    // TODO 15
    @Override
    public List<Student> findAboveAverageGpa() {
        return studentRepository.findAboveAverageGpa();
    }

    // TODO 17
    @Override
    public List<Student> findTopNInDepartment(String deptCode, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of students must be greater than zero");
        }
        return studentRepository.findTopNInDepartment(deptCode, n);
    }

    // TODO 18
    @Override
    public List<StudentSummary> getActiveSummaries() {
        return studentRepository.findActiveSummaries();
    }

    // TODO 19
    @Override
    public Page<Student> findActiveByDepartment(String deptCode, int pageIndex, int size) {
        if (pageIndex < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be greater than zero");
        }
        return studentRepository.findActiveByDepartment(deptCode, PageRequest.of(pageIndex, size));
    }

    //Todo 20
    @Override
    @Transactional
    public Student updateGpa(String code, double newGpa) {
        if (!Double.isFinite(newGpa) || newGpa < 0 || newGpa > 4) {
            throw new IllegalArgumentException("GPA must be between 0 and 4");
        }
        Student student = studentRepository.findByStudentCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Student with code " + code + " not found"));
        student.setGpa(newGpa);
        return studentRepository.save(student);
    }
}
