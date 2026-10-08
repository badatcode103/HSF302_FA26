package org.fa26.chapter6;

import org.fa26.chapter6.controller.HomeController;
import org.fa26.chapter6.controller.StudentController;
import org.fa26.chapter6.entity.Student;
import org.fa26.chapter6.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({StudentController.class, HomeController.class})
class StudentFrontendTests {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Nguyễn Văn An", "an@fpt.edu.vn", 20, "CNTT", 3.5);
        student.setId(1L);
        when(studentService.getMajors()).thenReturn(List.of("CNTT", "KTPM", "HTTT", "MMT", "ATTT"));
        when(studentService.findById(1L)).thenReturn(Optional.of(student));
    }

    @Test
    void listRendersLayoutDataAndEditLink() throws Exception {
        when(studentService.findAll()).thenReturn(List.of(student));
        mvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Student Manager")))
                .andExpect(content().string(containsString("Nguyễn Văn An")))
                .andExpect(content().string(containsString("/students/1/edit")))
                .andExpect(content().string(containsString("gpa-high")))
                .andExpect(content().string(containsString("/css/style.css")));
    }

    @Test
    void emptyListRendersEmptyState() throws Exception {
        when(studentService.findAll()).thenReturn(List.of());
        mvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Chưa có sinh viên nào.")));
    }

    @Test
    void createAndEditFormsRenderMajorsAndCorrectActions() throws Exception {
        mvc.perform(get("/students/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("action=\"/students/create\"")))
                .andExpect(content().string(containsString("value=\"CNTT\"")));
        mvc.perform(get("/students/1/edit"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("action=\"/students/1/edit\"")))
                .andExpect(content().string(containsString("Cập nhật sinh viên")))
                .andExpect(content().string(containsString("Nguyễn Văn An")));
    }

    @Test
    void detailRendersStudentAndDeleteAction() throws Exception {
        mvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("an@fpt.edu.vn")))
                .andExpect(content().string(containsString("action=\"/students/1/delete\"")))
                .andExpect(content().string(containsString("data-name=\"Nguyễn Văn An\"")));
    }

    @Test
    void invalidCreateRendersFieldErrorsWithoutSaving() throws Exception {
        mvc.perform(post("/students/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("student", "name", "email", "age", "major", "gpa"))
                .andExpect(content().string(containsString("is-invalid")))
                .andExpect(content().string(containsString("Tên không được để trống")))
                .andExpect(content().string(containsString("value=\"CNTT\"")));
        verify(studentService, never()).create(any());
    }

    @Test
    void invalidEditKeepsActionAndStudentId() throws Exception {
        mvc.perform(post("/students/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("student", "name"))
                .andExpect(content().string(containsString("action=\"/students/1/edit\"")));
        verify(studentService, never()).update(anyLong(), any());
    }
}
