package org.example.hsf302_classproject_1;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.example.hsf302_classproject_1.dao.EmployeeDAOImpl;
import org.example.hsf302_classproject_1.pojo.Employee;
import org.example.hsf302_classproject_1.pojo.enumPackage.Gender;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

@SpringBootApplication
public class Hsf302ClassProject1Application {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("employee_manager");
        System.out.println("EMF tao thanh cong!");

        EmployeeDAOImpl employeeDAO = new EmployeeDAOImpl();

        // ============ 1. CREATE ============
        System.out.println("\n--- STEP 1: CREATE ---");
        Employee newEmployee = new Employee();
        // Entity Lifecycle: newEmployee dang o trang thai NEW/TRANSIENT (chua co ID, chua lien ket voi DB)
        newEmployee.setFullName("Nguyen Van A");
        newEmployee.setEmail("nguyenvana@example.com");
        newEmployee.setSalary(new BigDecimal("15000000"));
        newEmployee.setGender(Gender.MALE);
        newEmployee.setHireDate(LocalDate.of(2022, 1, 15));
        newEmployee.setActive(true);

        Employee createdEmployee = null;
        try {
            createdEmployee = employeeDAO.create(newEmployee);
            // Entity Lifecycle: createdEmployee dang o trang thai MANAGED (duoc persist, co ID, EntityManager dong, 
            // se lai tro thanh DETACHED vi EntityManager da dong)
            System.out.println("✓ Da tao thanh cong: " + createdEmployee);
        } catch (ConstraintViolationException e) {
            System.out.println("✗ Error! Email already exists");
            emf.close();
            return;
        } catch (PersistenceException e) {
            System.out.println("✗ Error! Persistence exception occurs");
            emf.close();
            return;
        }
        // Entity Lifecycle: sau khi create() return, createdEmployee la DETACHED (EntityManager da dong, nhung con co ID, du lieu da luu trong DB)

        // ============ 2. READ ============
        System.out.println("\n--- STEP 2: READ (After Create) ---");
        Employee foundEmployee = employeeDAO.findById(Employee.class, createdEmployee.getId());
        // Entity Lifecycle: foundEmployee la MANAGED trong ngay EntityManager dang mo, sau khi close se la DETACHED
        if (foundEmployee != null) {
            System.out.println("✓ Tim thay nhan vien: " + foundEmployee);
        } else {
            System.out.println("✗ Khong tim thay nhan vien");
        }
        // Entity Lifecycle: foundEmployee dang o trang thai DETACHED (EntityManager da dong)

        // ============ 3. UPDATE ============
        System.out.println("\n--- STEP 3: UPDATE ---");
        foundEmployee.setSalary(new BigDecimal("18000000"));
        // Entity Lifecycle: foundEmployee van la DETACHED (dang ngoai EntityManager)
        Employee updatedEmployee = employeeDAO.update(foundEmployee);
        // Entity Lifecycle: trong update(), foundEmployee duoc merge() -> tro thanh MANAGED
        // updatedEmployee (ket qua tra ve) la MANAGED (trong transaction), la tham chieu moi
        // foundEmployee (cua) van con DETACHED (khong duoc EntityManager quan ly nua)
        System.out.println("✓ Da cap nhat thanh cong: " + updatedEmployee);
        // Entity Lifecycle: sau update() return, updatedEmployee tro thanh DETACHED (EntityManager da dong)

        // ============ 4. READ LAI DE KIEM TRA UPDATE ============
        System.out.println("\n--- STEP 4: READ LAI (After Update) ---");
        Employee readAfterUpdate = employeeDAO.findById(Employee.class, updatedEmployee.getId());
        // Entity Lifecycle: readAfterUpdate la MANAGED trong ngay EntityManager dang mo, sau khi close se la DETACHED
        if (readAfterUpdate != null) {
            System.out.println("✓ Tim thay nhan vien sau update: " + readAfterUpdate);
            System.out.println("  Luong moi: " + readAfterUpdate.getSalary());
        } else {
            System.out.println("✗ Khong tim thay nhan vien");
        }
        // Entity Lifecycle: readAfterUpdate dang o trang thai DETACHED (EntityManager da dong)

        // ============ 5. DELETE ============
        System.out.println("\n--- STEP 5: DELETE ---");
        employeeDAO.delete(updatedEmployee);
        // Entity Lifecycle: trong delete(), updatedEmployee duoc merge() de tro thanh MANAGED
        // sau do em.remove() -> entity tro thanh REMOVED (dang trong transaction)
        // sau commit() -> entity bi xoa khoi DB, khong the truy cap bang em.find()
        System.out.println("✓ Da xoa nhan vien co id: " + updatedEmployee.getId());
        // Entity Lifecycle: sau delete() return, updatedEmployee la REMOVED (da bi xoa khoi DB, EntityManager da dong)

        // ============ 6. READ LAI DE KIEM TRA DA XOA ============
        System.out.println("\n--- STEP 6: READ LAI (After Delete) ---");
        Employee readAfterDelete = employeeDAO.findById(Employee.class, updatedEmployee.getId());
        // Entity Lifecycle: readAfterDelete se la null vi entity da bi xoa khoi DB
        if (readAfterDelete == null) {
            System.out.println("✓ Xac nhan: Nhan vien da bi xoa (null)");
        } else {
            System.out.println("✗ Error: Nhan vien van con trong he thong");
        }

        System.out.println("\n--- KET THUC LUONG CRUD ---");
        emf.close();
    }

}
