package org.fa26.de190686.dao;

import java.util.List;

import org.fa26.de190686.pojo.Department;

public interface DepartmentDAO extends GenericDAO<Department, Long>{
    Department findDepartmentById(Long id);
    List<Department> findAllWithEmployees();
    List<Department> findAllWithNPlusOne();
}
