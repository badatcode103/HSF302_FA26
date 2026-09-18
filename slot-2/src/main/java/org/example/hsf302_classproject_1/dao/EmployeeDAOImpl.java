package org.example.hsf302_classproject_1.dao;

import java.math.BigDecimal;
import java.util.List;
import org.example.hsf302_classproject_1.pojo.Employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class EmployeeDAOImpl extends GenericDAOImpl<Employee, Long> implements EmployeeDAO {

    @Override
    public Employee findByEmail(String email) {
        // TODO Auto-generated method stub
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e WHERE e.email = :email",
                    Employee.class);
            query.setParameter("email", email);
            Employee employee = query.getSingleResult();
            return employee;
        } catch (NoResultException e) {
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Employee> findEmployeeWithSalary(BigDecimal salary) {
        // TODO Auto-generated method stub
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e WHERE e.salary > :salary",
                    Employee.class);
            query.setParameter("salary", salary);
            List<Employee> employees = query.getResultList();
            return employees;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Employee> findActiveEmployees() {
        // TODO Auto-generated method stub
        EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT e FROM Employee e WHERE e.active = TRUE",
                    Employee.class);
            List<Employee> employees = query.getResultList();
            return employees;
        } finally {
            em.close();
        }
    }

}
