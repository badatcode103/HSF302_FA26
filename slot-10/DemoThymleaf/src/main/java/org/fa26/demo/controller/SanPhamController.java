package org.fa26.demo.controller;

import org.fa26.demo.model.SanPham;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping("/sanpham")
public class SanPhamController {
    private final List<SanPham> danhsach = new CopyOnWriteArrayList<>();

    //GET - hien thi form rong
    @GetMapping("/them")
    public String showForm(Model model){
        model.addAttribute("sanPham", new SanPham());
        return "sanpham/form";
    }

    @PostMapping("/them")
    public String xuliForm(@ModelAttribute("sanPham") SanPham sanPham, RedirectAttributes redirectAttributes){
        danhsach.add(sanPham);
        redirectAttributes.addFlashAttribute("thongBao", "Thêm sản phẩm thành công");
        return "redirect:/sanpham/ket-qua";
    }

    @GetMapping("/ket-qua")
    public String showResult(Model model){
        model.addAttribute("danhSach", danhsach);
        return "sanpham/ket-qua";
    }
}
