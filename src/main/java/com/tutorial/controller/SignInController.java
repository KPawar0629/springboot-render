package com.tutorial.controller;

import com.tutorial.model.*;
import com.tutorial.service.EventService;
import com.tutorial.service.TicketDetailsService;
import com.tutorial.service.TicketMasterService;
import com.tutorial.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class SignInController {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private TicketDetailsService ticketDetailsService;
    @Autowired
    private TicketMasterService ticketMasterService;

    @GetMapping("/signin")
    public String showSignInPage() {
        return "signin";
    }

    @PostMapping("/signin")
    public String handleSignIn(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        Optional<User> user = userService.findUserByEmail(email);

        if(user.isPresent() && user.get().getPassword().equals(password)) {
            session.setAttribute("loggedInUser", user.get());
            model.addAttribute("loggedInUser", user.get());
            model.addAttribute("message", "Login Successful");
            return "redirect:/dashboard";
        } else {
            model.addAttribute("message", "Invalid email or password");
            return "signin";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Event> events = eventService.findAllEvents();
        model.addAttribute("events", events);
        model.addAttribute("loggedInUser", loggedInUser);
        session.setAttribute("loggedInUser", loggedInUser);
        return "dashboard";

//        if(loggedInUser != null) {
//            model.addAttribute("userEmail", loggedInUser.getEmail());
//            model.addAttribute("userName", loggedInUser.getFullName());
//            return "dashboard";
//        }
    }

    @GetMapping("/actions_ticket")
    public String showCheckInOrPayment(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        session.setAttribute("loggedInUser", loggedInUser);
        return "checkin_list";

//        if(loggedInUser != null) {
//            model.addAttribute("userEmail", loggedInUser.getEmail());
//            model.addAttribute("userName", loggedInUser.getFullName());
//            return "dashboard";
//        }
    }

    @GetMapping("/signout")
    public String handleSignOut(HttpSession session) {
        session.invalidate();
        return "redirect:/dashboard";
    }

    @PostMapping("/actions_tickets/checkIn")
    public String handleCheckIn(
            @RequestParam int checkInCode,
            Model model,
            HttpSession session
    ) {
        User loggedInUser = (User) model.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null) {
                return "redirect:/dashboard";
            }
        }
        Optional<TicketDetails> details = Optional.ofNullable(ticketDetailsService.findByUniqueId(checkInCode));
        if (details.isPresent() && details.get().getCheckedIn() != 1) {
            TicketDetails ticketDetails = details.get();
            ticketDetails.setCheckedIn(1);
            ticketDetails.setCheckedInBy(loggedInUser.getUserId());
            ticketDetails.setCheckedInAt(new Date().toString());
            ticketDetailsService.addOrUpdateTicketDetails(ticketDetails);
            return "redirect:/actions_ticket";
        } else {
            return "redirect:/dashboard";
        }
    }

//    @PostMapping("/actions_ticket/payment")
//    public String handlePayment(
//            @RequestParam String firstName,
//            Model model,
//            HttpSession session
//    ) {
//        User loggedInUser = (User) model.getAttribute("loggedInUser");
//        if (loggedInUser == null) {
//            loggedInUser = (User) session.getAttribute("loggedInUser");
//            if (loggedInUser == null) {
//                return "redirect:/dashboard";
//            }
//        }
//
//        List<TicketMaster> ticketers = ticketMasterService.findTicketMasterByFirstName(firstName);
//        model.addAttribute("firstName", firstName);
//        model.addAttribute("ticketers", ticketers);
//        model.addAttribute("loggedInUser", loggedInUser);
//        return "/payment_list";
//    }

    @GetMapping("/actions_ticket/payments/{masterId}")
    public String showPaymentReciept(@PathVariable String masterId, Model model, HttpSession session) {
        TicketMaster master = ticketMasterService.findTicketMasterById(masterId);
        model.addAttribute("master", master);
        List<TicketDetails> details = ticketDetailsService.findTicketDetailsByTicketId(master.getTicketMasterId());
        model.addAttribute("details", details);

        if(session.getAttribute("loggedInUser") != null) {
            return "/payment_detail";
        } else {
            return "redirect:/signin";
        }
    }


    // Payments

    @GetMapping("/payment_list")
    public String getAllTicket(Model model, HttpSession session) {
        List<TicketMaster> masters = ticketMasterService.findAllTicketMasters();
        Map<String, User> users = new HashMap<>();
        model.addAttribute("ticketers", masters);
        for(TicketMaster master : masters) {
            if (master.getPaymentReceived() == 1) {
                Optional<User> user = userService.getUserByUserId(master.getPaymentReceivedBy());
                if (user.isPresent()) {
                    users.put(master.getPaymentReceivedBy(), user.get());
                }
            }
        }
        model.addAttribute("users", users);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);  // Ensure this is added
        if(session.getAttribute("loggedInUser") != null) {
            return "/payment_list";
        } else {
            return "redirect:/signin";
        }
    }

    @GetMapping("/payment/{masterId}")
    public String confirmPayment(@PathVariable String masterId, Model model, HttpSession session) {
        TicketMaster master = ticketMasterService.findTicketMasterById(masterId);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<TicketDetails> details = ticketDetailsService.findTicketDetailsByTicketId(master.getTicketMasterId());
        for (TicketDetails ticketDetails : details) {
            ticketDetails.setPaidStatus(1);
            ticketDetailsService.addOrUpdateTicketDetails(ticketDetails);
        }
        master.setPaymentReceived(1);
        master.setPaymentReceivedBy(loggedInUser.getUserId());
        master.setPaymentReceivedAt(new Date().toString());
        ticketMasterService.updateTicketMaster(master);

        return "redirect:/payment_list";
    }

    @GetMapping("/checkin")
    public String getAllTicketDetails(Model model, HttpSession session) {
        List<TicketDetails> details = ticketDetailsService.findAllTicketDetails();
        model.addAttribute("details", details);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        if(session.getAttribute("loggedInUser") != null) {
            return "/checkin_list";
        } else {
            return "redirect:/signin";
        }
    }

    @PostMapping("/confirmCheckIn")
    public String confirmCheckIn(@RequestParam("ticketIds") List<String> ticketIds, Model model, HttpSession session) {

        if(session.getAttribute("loggedInUser") != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            for (String ticketId : ticketIds) {
                Optional<TicketDetails> details = ticketDetailsService.findTicketDetailsByDetailsId(ticketId);

                if (details.isPresent()) {
                    TicketDetails ticketDetails = details.get();
                    ticketDetails.setCheckedIn(1);
                    ticketDetails.setCheckedInBy(loggedInUser.getUserId());
                    ticketDetails.setCheckedInAt(new Date().toString());
                    ticketDetailsService.addOrUpdateTicketDetails(ticketDetails);
                }
            }

            model.addAttribute("loggedInUser", loggedInUser);
            return "redirect:/checkin";
        } else {
            return "redirect:/signin";
        }
    }
}
