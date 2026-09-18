package org.example.hsf302_classproject_1.dao;

import java.math.BigDecimal;
import java.util.List;

import org.example.hsf302_classproject_1.pojo.Employee;

public interface EmployeeDAO extends GenericDAO<Employee,Long>{
    Employee findByEmail(String email);

    List<Employee> findEmployeeWithSalary(BigDecimal salary);

    List<Employee> findActiveEmployees();

}
