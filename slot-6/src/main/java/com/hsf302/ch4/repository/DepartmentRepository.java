package com.hsf302.ch4.repository;

import com.hsf302.ch4.pojo.Department;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    //11d
    List<Department> findByStudentsIsEmpty();
}
