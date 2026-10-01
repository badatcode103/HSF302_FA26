package com.hsf302.ch4.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hsf302.ch4.pojo.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Additional query methods can be defined here if needed

}
