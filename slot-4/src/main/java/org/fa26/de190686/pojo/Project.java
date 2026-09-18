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
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    //•	Project: id, projectCode (unique), projectName,
    // budget (BigDecimal), startDate (LocalDate), endDate (LocalDate, có thể null nếu dự án chưa kết thúc).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String projectCode;
    private String projectName;
    private BigDecimal budget;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees = new HashSet<>();

    // Do not use id because it is generated only after the entity is persisted;
    // projectCode is the stable, unique business key used to identify a Project.
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Project project)) {
            return false;
        }
        return projectCode != null && projectCode.equals(project.projectCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(projectCode);
    }
}
