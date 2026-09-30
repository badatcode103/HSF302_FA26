package com.hsf302.ch4.repository;

import com.hsf302.ch4.dto.DepartmentStatDTO;
import com.hsf302.ch4.pojo.Department;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    //11d
    List<Department> findByStudentsIsEmpty();

    //todo 14
    @Query("""
        SELECT new com.hsf302.ch4.dto.DepartmentStatDTO(
            d.code, d.name, COUNT(s), AVG(s.gpa))
            FROM Department d LEFT JOIN d.students s
            GROUP BY d.code, d.name
            ORDER BY d.code ASC       
          """)
    List<DepartmentStatDTO> findDepartmentStats();
}
