package com.tutorial.controller;

import com.tutorial.model.Event;
import com.tutorial.model.User;
import com.tutorial.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping("/user_list")
    public String getAllEvents(Model model, HttpSession session) {
        System.out.println("1");
        List<User> users = service.findAllUsers();
        System.out.println("2");
        model.addAttribute("users", users);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);  // Ensure this is added
        if(session.getAttribute("loggedInUser") != null) {
            return "/user_list";
        } else {
            return "redirect:/signin";
        }
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        return service.findUserById(userId).get();
    }

    @GetMapping("/userName/{userName}")
    public List<User> findUserWithFullName(@PathVariable String userName) {
        return service.findUserByFullName(userName);
    }

    @PutMapping("/user/modify")
    public User modifyUser(@RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return service.deleteUser(userId);
    }

}
