package com.hsf302.ch4.runner;

import com.hsf302.ch4.pojo.Department;
import com.hsf302.ch4.pojo.Gender;
import com.hsf302.ch4.pojo.Student;
import com.hsf302.ch4.repository.DepartmentRepository;
import com.hsf302.ch4.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        Department se = new Department("SE", "Software Engineering");
        Department ai = new Department("AI", "Artificial Intelligence");
        Department ia = new Department("IA", "Information Assurance");
        Department gd = new Department("GD", "Graphic Design");
        departmentRepository.saveAll(List.of(se, ai, ia, gd));

        Student an = student("SE001", "Nguyen Van An", "an.nv@fpt.edu.vn", Gender.MALE,
                "2005-03-15", 3.2, true);
        Student binh = student("SE002", "Tran Thi Binh", "binh.tt@fpt.edu.vn", Gender.FEMALE,
                "2004-07-22", 3.8, true);
        Student cuong = student("SE003", "Le Van Cuong", "cuong.lv@fpt.edu.vn", Gender.MALE,
                "2003-11-05", 2.5, false);
        Student dung = student("AI001", "Pham Thi Dung", "dung.pt@fpt.edu.vn", Gender.FEMALE,
                "2006-01-10", 3.5, true);
        Student em = student("AI002", "Hoang Van Em", "em.hv@gmail.com", Gender.MALE,
                "2002-09-30", 2.8, true);
        Student hoa = student("AI003", "Vo Thi Hoa", "hoa.vt@fpt.edu.vn", Gender.FEMALE,
                "2005-05-18", 3.9, true);
        Student giang = student("IA001", "Dang Van Giang", "giang.dv@gmail.com", Gender.MALE,
                "2001-12-01", 1.9, false);
        Student lan = student("IA002", "Bui Thi Lan", "lan.bt@fpt.edu.vn", Gender.FEMALE,
                "2004-02-14", 3.1, true);
        Student mai = student("SE004", "Nguyen Thi Mai", "mai.nt@fpt.edu.vn", Gender.FEMALE,
                "2003-08-08", 3.6, true);
        Student nam = student("IA003", "Do Van Nam", null, Gender.MALE,
                "2005-10-20", 2.2, true);

        se.addStudent(an);
        se.addStudent(binh);
        se.addStudent(cuong);
        se.addStudent(mai);
        ai.addStudent(dung);
        ai.addStudent(em);
        ai.addStudent(hoa);
        ia.addStudent(giang);
        ia.addStudent(lan);
        ia.addStudent(nam);
        studentRepository.saveAll(List.of(an, binh, cuong, dung, em, hoa, giang, lan, mai, nam));
    }

    private Student student(String code, String fullName, String email, Gender gender,
                            String dob, double gpa, boolean active) {
        return new Student(code, fullName, email, gender, LocalDate.parse(dob), gpa, active);
    }
}
