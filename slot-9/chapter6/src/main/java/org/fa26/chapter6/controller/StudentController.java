package org.fa26.chapter6.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fa26.chapter6.entity.Student;
import org.fa26.chapter6.service.StudentService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    private static final String FORM_VIEW = "students/form";

    @ModelAttribute("majors")
    public List<String> majors() {
        return studentService.getMajors();
    }

    private String formView(Model model, boolean isEdit) {
        model.addAttribute("isEdit", isEdit);
        model.addAttribute("pageTitle", isEdit ? "Cập nhật sinh viên" : "Thêm sinh viên mới");
        return FORM_VIEW;
    }

    //Read all student
    @GetMapping
    public String list(Model model){
        model.addAttribute("students", studentService.findAll());
        return "students/list";
    }

    //Read one student
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        return studentService.findById(id)
                .map(student -> {
                    model.addAttribute("student", student);
                    return "students/detail";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
                    return "redirect:/students";
                });
    }

    //create student
    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("student", new Student());
        return formView(model, false);
    }

    @PostMapping("/create")
    public String create(Model model, @Valid @ModelAttribute("student") Student student, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        // 1. Kiểm tra nghiệp vụ: email trùng (chỉ khi email đã hợp lệ về định dạng)
        if (!bindingResult.hasFieldErrors("email")
                && studentService.isEmailTaken(student.getEmail(), null)) {
            bindingResult.rejectValue("email", "duplicate", "Email đã tồn tại");
        }
        // 2. Có lỗi → quay lại form (KHÔNG redirect để giữ dữ liệu + lỗi)
        if (bindingResult.hasErrors()) {
            return formView(model, false);
        }
        // 3. Lưu DB — vẫn bắt lỗi UNIQUE phòng trường hợp 2 người submit cùng lúc
        try {
            studentService.create(student);
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("email", "duplicate", "Email đã tồn tại");
            return formView(model, false);
        }
        redirectAttributes.addFlashAttribute("successMsg", "Thêm sinh viên thành công!");
        return "redirect:/students";
    }

    //update student
    @GetMapping({"/{id}/edit", "/{id}/update"})
    public String showUpdateForm(Model model,
                                 RedirectAttributes redirectAttributes,
                                 @PathVariable("id") Long id){
        return studentService.findById(id).map(student -> {
            model.addAttribute("student", student);
            return formView(model, true);
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên");
            return "redirect:/students";
        });
    }

    @PostMapping({"/{id}/edit", "/{id}/update"})
    public String update(Model model,
                         @PathVariable("id") Long id,
                         @Valid @ModelAttribute("student") Student student,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes
                         ) {
        student.setId(id);
        if(!bindingResult.hasFieldErrors("email")
        && studentService.isEmailTaken(student.getEmail(),id)){
            bindingResult.rejectValue("email", "duplicate", "Email đã được sinh viên khác sử dụng");
        }
        if(bindingResult.hasErrors()){
            return formView(model, true);
        }
        try {
            if (studentService.update(id, student)) {
                redirectAttributes.addFlashAttribute("successMsg", "Cập nhật thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
            }
        } catch (DataIntegrityViolationException e) {
            bindingResult.rejectValue("email", "duplicate", "Email đã được sinh viên khác sử dụng");
            return formView(model, true);
        }
        return "redirect:/students";
    }

    //delete sinh vien
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if (studentService.delete(id)) {
            redirectAttributes.addFlashAttribute("successMsg", "Xóa thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMsg", "Không tìm thấy sinh viên ID: " + id);
        }
        return "redirect:/students";
    }

}
