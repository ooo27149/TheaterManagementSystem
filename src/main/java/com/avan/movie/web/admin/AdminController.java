package com.avan.movie.web.admin;

import com.avan.movie.po.Admin;
import com.avan.movie.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/admin_list")
    public String adminList(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC)
                                        Pageable pageable, Admin admin, Model model) {
        model.addAttribute("page", adminService.listAdmin(pageable, admin));
        return "admin/admin_list";
    }
    @PostMapping("/admin_list/search")
    public String adminSearch(@PageableDefault(size = 8, sort = {"id"}, direction = Sort.Direction.DESC)
                                        Pageable pageable, Admin admin, Model model) {
        model.addAttribute("page", adminService.listAdmin(pageable, admin));
        return "admin/admin_list :: adminList";
    }

    @GetMapping("/admin_list/admin_edit")
    public String adminEdit(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/admin_edit";
    }

    @GetMapping("/admin_list/{id}/admin_edit")
    public String adminUpdate(@PathVariable Long id, Model model) {
        model.addAttribute("admin", adminService.getAdmin(id));
        return "admin/admin_edit";
    }


    @PostMapping("/admin_save")
    public String adminSave(@Valid Admin admin, BindingResult bindingResult,
                            @RequestParam("file") MultipartFile file,
                            @RequestParam("confirmPassword") String confirmPassword,
                            RedirectAttributes attributes) {

        Admin temp = adminService.getAdminByPhone(admin.getPhone());
        if (temp != null) {
            bindingResult.rejectValue("phone", "phoneError", "该手机账户已存在");
        }
        if (!admin.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("phone", "phoneError", "两次输入密码不一致，请重新输入");
        }else if (admin.getPassword().equals(confirmPassword) && admin.getPassword() != null && !"".equals(admin.getPassword())){
            admin.setPassword(confirmPassword);
        }

        if (file.isEmpty()) {
            bindingResult.rejectValue("phone", "phoneError", "请上传头像");
        } else {
            saveAvatar(file, admin);
        }

        if (bindingResult.hasErrors()) {
            return "admin/admin_edit";
        }
        admin.setUpdateTime(new Date());

        Admin a = adminService.saveAdmin(admin);
        if (a == null) {
            attributes.addFlashAttribute("message", "操作失败");

        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }

        return "redirect:/admin/admin_list";
    }

    private static void saveAvatar(MultipartFile file, Admin admin) {
        String fileName = file.getOriginalFilename();
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if(!path.exists()) {
                path = new File("");
            }
            File upload = new File(path.getAbsolutePath(),"static/avatars/");
            if(!upload.exists()) {
                upload.mkdirs();
            }
            File dest = new File(path.getAbsolutePath(),"static/avatars/"+ admin.getNickname() + fileName);
            file.transferTo(dest); //保存文件
            admin.setAvatar("/avatars/" + admin.getNickname() + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @PostMapping("/admin_save/{id}")
    public String adminUpdate(Admin temp,
                              @PathVariable Long id,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("confirmPassword") String confirmPassword,
                              HttpServletRequest request, RedirectAttributes attributes) {

        if (file.isEmpty()
                && (temp.getPassword() == null || "".equals(temp.getPassword()))
                && (confirmPassword == null || "".equals(confirmPassword))) {
            attributes.addFlashAttribute("message", "您未作出任何修改，保存失败");
            return "redirect:/admin/info";
        }

        Admin admin = adminService.getAdmin(id);
        if (!file.isEmpty()) {
            saveAvatar(file, admin);
            attributes.addFlashAttribute("message1", "头像更新成功");
        }

        if (!temp.getPassword().equals(confirmPassword)) {
            attributes.addFlashAttribute("admin", admin);
            attributes.addFlashAttribute("message", "两次输入密码不一致，请重新输入");
            return "redirect:/admin/info";
        }else if (temp.getPassword().equals(confirmPassword) && temp.getPassword() != null && !"".equals(temp.getPassword())){
            admin.setPassword(confirmPassword);
            admin.setLevel(temp.getLevel());
            admin.setNickname(temp.getNickname());
            attributes.addFlashAttribute("message", "密码修改成功");
        }
        admin.setUpdateTime(new Date());
        adminService.updateAdmin(id, admin);
        if(request.getSession().getAttribute("level") == null){
            return "redirect:/admin/info";
        }
        return "redirect:/admin/admin_list";
    }

    @GetMapping("/admin_list/{id}/admin_delete")
    public String adminDelete(@PathVariable Long id, RedirectAttributes attributes) {
        adminService.deleteAdmin(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/admin_list";
    }
}
