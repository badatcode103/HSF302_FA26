package org.fa26.de190686.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    //•	Employee: id, fullName, salary (BigDecimal), hireDate (LocalDate),
    // email (unique, not null), gender (enum Gender { MALE, FEMALE, OTHER }), active (boolean, mặc định true).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private BigDecimal salary;
    private LocalDate hireDate;
    @Column (unique = true, nullable = false)
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private boolean active = true;

    @ManyToMany
    @JoinTable(name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private Set<Project> projects = new HashSet<>();

    public void assignProject(Project project) {
        this.projects.add(project);
        project.getEmployees().add(this);
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
        project.getEmployees().remove(this);
    }

    // Do not use id because it is generated only after the entity is persisted;
    // email is the stable, unique business key used to identify an Employee.
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Employee employee)) {
            return false;
        }
        return email != null && email.equals(employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
