package org.fa26.de190686.dao.daoImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.fa26.de190686.dao.DepartmentDAO;
import org.fa26.de190686.dao.GenericDAOImpl;
import org.fa26.de190686.pojo.Department;
import org.fa26.de190686.util.JPAutil;

import java.util.List;

public class DepartmentDAOIpml extends GenericDAOImpl<Department, Long> implements DepartmentDAO {

    @Override
    public Department findDepartmentById(Long id) {
        EntityManager entityManager = JPAutil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT d FROM Department d JOIN FETCH d.employees WHERE d.id = :id";
            TypedQuery<Department> query = entityManager.createQuery(jpql, Department.class);
            query.setParameter("id", id);
            List<Department> department = query.getResultList();
            return department.isEmpty() ? null : department.get(0);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Department> findAllWithEmployees() {
        EntityManager em = JPAutil.getEntityManager();
        try {
            String jpql = "SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.employees";
            TypedQuery<Department> query = em.createQuery(jpql, Department.class);
            List<Department> result = query.getResultList();
            return result;
        } finally {
            em.close();
        }
    }

    public List<Department> findAllWithNPlusOne() {
        EntityManager em = JPAutil.getEntityManager();

        try {
            // Query số 1: lấy tất cả Department
            List<Department> departments = em.createQuery(
                    "SELECT d FROM Department d",
                    Department.class).getResultList();

            // Mỗi Department phát sinh thêm một query lấy Employee
            for (Department department : departments) {
                System.out.println(
                        department.getName()
                                + ": "
                                + department.getEmployees().size()
                                + " employees");
            }

            return departments;
        } finally {
            em.close();
        }
    }
}
