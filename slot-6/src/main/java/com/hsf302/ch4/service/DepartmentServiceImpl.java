package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.DepartmentStatDTO;
import com.hsf302.ch4.pojo.Department;
import com.hsf302.ch4.repository.DepartmentRepository;
import com.hsf302.ch4.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final StudentRepository studentRepository;

    @Override
    public long count() {
        return departmentRepository.count();
    }

    @Override
    public boolean existsById(Long id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public List<Department> findDepartmentsWithoutStudents() {
        return departmentRepository.findByStudentsIsEmpty();
    }
    
    //todo 14
    @Override
    public List<DepartmentStatDTO> findDepartmentStats() {
        return departmentRepository.findDepartmentStats();
    }

    // TODO 16
    @Override
    public Optional<Department> findByCode(String code) {
        return departmentRepository.findByCode(code);
    }

    @Override
    public Department getWithStudents(String code) {
        return departmentRepository.findByCodeWithStudents(code)
                .orElseThrow(() -> new IllegalArgumentException("Department not found: " + code));
    }

    @Override
    @Transactional
    public int transferStudentsAndDeleteDepartment(String oldCode, String newCode) {
        Department oldDept = departmentRepository.findByCode(oldCode)
                .orElseThrow(() -> new IllegalArgumentException("Old department not found: " + oldCode));
        Department newDept = departmentRepository.findByCode(newCode)
                .orElseThrow(() -> new IllegalArgumentException("New department not found: " + newCode));

        int transferredCount = studentRepository.transferStudentsToNewDepartment(oldDept, newDept);
        departmentRepository.delete(oldDept);
        return transferredCount;
    }
}
    

