package com.hsf302.ch4.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_code", nullable = false, unique = true, length = 20)
    private String studentCode;

    @Column(name = "full_name", nullable = false, length = 100, columnDefinition = "NVARCHAR(100)")
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dob;

    @Column(nullable = false)
    private double gpa;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    public Student(String studentCode, String fullName, String email, Gender gender,
                   LocalDate dob, double gpa, boolean active) {
        this.studentCode = studentCode;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.gpa = gpa;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Student{id=" + id
                + ", studentCode='" + studentCode + '\''
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + ", gender=" + gender
                + ", dob=" + dob
                + ", gpa=" + gpa
                + ", active=" + active
                + '}';
    }

    @ManyToMany
    @JoinTable (
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    public void enrollInCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

    public void unenrollFromCourse(Course course) {
        courses.remove(course);
        course.getStudents().remove(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Student other)) {
            return false;
        }
        return studentCode != null && studentCode.equals(other.studentCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(studentCode);
    }
}
