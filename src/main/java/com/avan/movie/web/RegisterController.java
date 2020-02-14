package com.avan.movie.web;

import com.avan.movie.po.Client;
import com.avan.movie.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class RegisterController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/login")
    public String toLogin(HttpSession session) {
//        session.removeAttribute("client");
        return "login";
    }

    @PostMapping("/verify")
    public String clientLogin(@RequestParam String phone, @RequestParam String password,
                              HttpSession session, RedirectAttributes attributes, Model model) {
        Client client = clientService.checkClient(phone, password);
        if (client != null) {
            client.setPassword(null);
            session.setAttribute("client", client);
            model.addAttribute("client", client);
            return "redirect:/tweets";
        } else {
            attributes.addFlashAttribute("message", "username or password wrong");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("client");
        return "redirect:/tweets";
    }

    @GetMapping("/client/info")
    public String info(HttpSession session, Model model) {
        model.addAttribute("client", session.getAttribute("client"));
        return "client_info";
    }

    @GetMapping("/client/order")
    public String order(HttpSession session, Model model) {
        model.addAttribute("client", session.getAttribute("client"));
        return "client_order";
    }

    @GetMapping("/register")
    public String clientRegister(Model model) {
        model.addAttribute("client", new Client());
        return "client_register";
    }

    @PostMapping("/client_save")
    public String clientSave(@Valid Client client, BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("confirmPassword") String confirmPassword,
                             RedirectAttributes attributes) {

        Client temp = clientService.getClientByPhone(client.getPhone());
        if (temp != null) {
            bindingResult.rejectValue("phone", "phoneError", "该手机号码已被注册");
        }
        if (client.getPhone().length() != 11) {
            bindingResult.rejectValue("phone", "phoneError", "请输入正确的手机号码");
        }
        if (client.getPassword().length() < 6) {
            bindingResult.rejectValue("phone", "phoneError", "密码长度不允许少于6位");
        }
        if (!client.getPassword().equals(confirmPassword)) {
            bindingResult.rejectValue("phone", "phoneError", "两次输入密码不一致，请重新输入");
        } else if (client.getPassword().equals(confirmPassword) && client.getPassword() != null && !"".equals(client.getPassword())) {
            client.setPassword(confirmPassword);
        } else {
            bindingResult.rejectValue("phone", "phoneError", "设置密码错误");
        }

        if (file.isEmpty()) {
            bindingResult.rejectValue("phone", "phoneError", "请上传头像");
        }
        if (bindingResult.hasErrors()) {
            return "client_register";
        }

        String fileName = file.getOriginalFilename();
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            File upload = new File(path.getAbsolutePath(), "static/avatarsClient/");
            if (!upload.exists()) {
                upload.mkdirs();
            }
            File dest = new File(path.getAbsolutePath(), "static/avatarsClient/" + client.getPhone() + fileName);
            file.transferTo(dest); //保存文件
            client.setAvatar("/avatarsClient/" + client.getPhone() + fileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Client a = clientService.saveClient(client);
        if (a == null) {
            attributes.addFlashAttribute("message", "注册失败");

        } else {
            attributes.addFlashAttribute("message1", "注册成功，请登录");
        }
        return "redirect:/login";
    }

    @PostMapping("/client_save/{id}")
    public String adminUpdate(Client temp,
                              @PathVariable Long id,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("confirmPassword") String confirmPassword,
                              HttpServletRequest request,
                              RedirectAttributes attributes) {

        if (file.isEmpty()
                && (temp.getPassword() == null || "".equals(temp.getPassword()))
                && (confirmPassword == null || "".equals(confirmPassword))) {
            attributes.addFlashAttribute("messageError", "您未作出任何修改，保存失败");
            return "redirect:/client/info";
        }

        Client client = clientService.getClient(id);

        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            try {
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                if (!path.exists()) {
                    path = new File("");
                }
                File upload = new File(path.getAbsolutePath(), "static/avatarsClient/");
                if (!upload.exists()) {
                    upload.mkdirs();
                }
                File dest = new File(path.getAbsolutePath(), "static/avatarsClient/" + client.getPhone() + fileName);
                file.transferTo(dest); //保存文件
                client.setAvatar("/avatarsClient/" + client.getPhone() + fileName);
                attributes.addFlashAttribute("message1", "头像更新成功");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (!temp.getPassword().equals(confirmPassword)) {
            attributes.addFlashAttribute("client", client);
            attributes.addFlashAttribute("messageError", "两次输入密码不一致，请重新输入");
            return "redirect:/client/info";
        } else if (temp.getPassword().equals(confirmPassword) && temp.getPassword() != null && !"".equals(temp.getPassword())) {
            client.setPassword(confirmPassword);
            attributes.addFlashAttribute("message", "密码修改成功");
        }
        clientService.updateClient(id, client);
        return "redirect:/client/info";
    }


}
