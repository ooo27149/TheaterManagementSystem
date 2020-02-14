package com.avan.movie.web;

import com.avan.movie.po.Admin;
import com.avan.movie.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileUploadController {

    @Autowired
    AdminService adminService;

//    @GetMapping("/upload_html")
    public String uploadHtml() {
        return "upfile";
    }

    //处理文件上传
//    @PostMapping(value = "/upload/{id}")
    public String uploadImg(@RequestParam("file") MultipartFile file, @PathVariable Long id, HttpServletRequest request) {

        if (file.isEmpty()) {
            return "false";
        }
        Admin a = adminService.getAdmin(id);

        String fileName = file.getOriginalFilename();//获取名字
        a.setAvatar("/avatars/" + fileName);
        adminService.updateAdmin(id, a);

        String path = "/Users/ooo27149/Documents/Graduation Project/GdemoV1.0/src/main/resources/static/avatars";

        File dest = new File(path + "/" + fileName);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest); //保存文件

            return "redirect:/admin/admin_list";
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return "上传失败!";
        }
    }

}
