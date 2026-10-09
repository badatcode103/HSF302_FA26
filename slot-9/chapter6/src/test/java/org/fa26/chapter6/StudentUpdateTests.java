package org.fa26.chapter6;

import org.fa26.chapter6.entity.Student;
import org.fa26.chapter6.repository.StudentRepository;
import org.fa26.chapter6.service.StudentService;
import org.fa26.chapter6.service.StudentServiceIpml;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(StudentServiceIpml.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class StudentUpdateTests {
    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void updateCommitsEveryEditableField() {
        Student original = studentService.create(
                new Student("Original Student", "original@example.com", 20, "CNTT", 3.0));
        Student changes = new Student("Updated Student", "updated@example.com", 23, "KTPM", 3.8);

        assertTrue(studentService.update(original.getId(), changes));

        // Read in a new transaction so in-memory changes cannot make this test pass.
        Student saved = studentRepository.findById(original.getId()).orElseThrow();
        assertAll(
                () -> assertEquals(original.getId(), saved.getId()),
                () -> assertEquals(changes.getName(), saved.getName()),
                () -> assertEquals(changes.getEmail(), saved.getEmail()),
                () -> assertEquals(changes.getAge(), saved.getAge()),
                () -> assertEquals(changes.getMajor(), saved.getMajor()),
                () -> assertEquals(changes.getGpa(), saved.getGpa()),
                () -> assertEquals(1L, studentRepository.count())
        );
    }

    @Test
    void updateMissingStudentReturnsFalseWithoutCreatingRecord() {
        assertFalse(studentService.update(-1L,
                new Student("Missing Student", "missing@example.com", 20, "CNTT", 3.0)));
        assertEquals(0L, studentRepository.count());
    }

    @Test
    void duplicateEmailFailsAndRollsBackChanges() {
        Student original = studentService.create(
                new Student("Original Student", "original@example.com", 20, "CNTT", 3.0));
        studentService.create(new Student("Other Student", "other@example.com", 21, "HTTT", 3.2));

        assertThrows(DataIntegrityViolationException.class, () -> studentService.update(original.getId(),
                new Student("Updated Student", "other@example.com", 23, "KTPM", 3.8)));

        Student saved = studentRepository.findById(original.getId()).orElseThrow();
        assertEquals("Original Student", saved.getName());
        assertEquals("original@example.com", saved.getEmail());
        assertEquals(20, saved.getAge());
        assertEquals("CNTT", saved.getMajor());
        assertEquals(3.0, saved.getGpa());
    }
}
