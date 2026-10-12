package org.fa26.demo.controller;

import org.fa26.demo.model.SinhVien;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SinhVienController {
    @GetMapping("/sinhvien")
    public String danhsach(Model model){
        List<SinhVien> danhsach = List.of(
                new SinhVien("SV001","Nguyen Van An",8.5),
                new SinhVien("SV002","Le Thi Rieng",6.5),
                new SinhVien("SV003","Mai An Cut",7.8)
        );
        model.addAttribute("listSinhVien",danhsach);
        model.addAttribute("tieuDe", "Danh sách sinh viên");
        return "sinhvien/danh-sach";
    }

}
