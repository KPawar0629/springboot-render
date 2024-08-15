package com.tutorial.controller;

import com.tutorial.model.User;
import org.springframework.ui.Model;
import com.tutorial.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignUpPage(Model model) {
        System.out.println("Why did this happen?");
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignUp(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String phone,
            @RequestParam String code,
            Model model,
            RedirectAttributes redirectAttributes) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> existingUser = userService.findUserByEmail(email);

        if(userService.userCount() >= 15) {
            model.addAttribute("error", "Too many users! If you are a official member, please delete some users.");
            return "signup";
        }

        if (existingUser.isPresent()) {
            model.addAttribute("error", "Email already exists");
            return "signup";
        }

        User newUser = new User();
        newUser.setUserId(UUID.randomUUID().toString().split("-")[0]);
        newUser.setFullName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhone(phone);
        newUser.setAccount_created(new Date().toString());

        userService.addUser(newUser);

        redirectAttributes.addFlashAttribute("message", "User Signup completed! Thanks for registering with Sangam.");

        return "redirect:/signin";
    }

    public int newCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return code;
    }
}
