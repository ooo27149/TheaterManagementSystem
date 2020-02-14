package com.avan.movie.web.admin;

import com.avan.movie.po.Admin;
import com.avan.movie.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {


    @Autowired
    private AdminService adminService;

    @GetMapping
    public String loginPage(){

        return "admin/login";
    }

    @GetMapping("/loginn")
    public String loginTest(){

        return "admin/loginTest";
    }

    @PostMapping("/login")
    public String login(@RequestParam String phone, @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes){
        Admin admin = adminService.checkAdmin(phone, password);
        if (admin != null) {
            admin.setPassword(null);
            session.setAttribute("admin", admin);
            if (admin.getLevel().equals(("管理层")) || admin.getLevel().equals(("admin"))) {
                session.setAttribute("level", admin);
            }
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "手机号码或密码错误，请重试");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("admin");
        if (session.getAttribute("level") != null) {
            session.removeAttribute("level");
        }
        return "redirect:/admin";
    }

    @GetMapping("/info")
    public String info(HttpSession session, Model model) {
        model.addAttribute("admin", session.getAttribute("admin"));
        return "admin/admin_info";
    }




}



















