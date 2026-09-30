package com.hsf302.ch4.service;

import com.hsf302.ch4.dto.DepartmentStatDTO;
import com.hsf302.ch4.pojo.Department;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    long count();

    boolean existsById(Long id);

    List<Department> findDepartmentsWithoutStudents();
    

    //todo 14
    List<DepartmentStatDTO> findDepartmentStats();

    // TODO 16
    Optional<Department> findByCode(String code);

    Department getWithStudents(String code);

    public int transferStudentsAndDeleteDepartment(String oldCode, String newCode) ;

}
