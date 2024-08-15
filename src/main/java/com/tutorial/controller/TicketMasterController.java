package com.tutorial.controller;

import com.tutorial.model.Event;
import com.tutorial.model.EventPricing;
import com.tutorial.model.TicketDetails;
import com.tutorial.model.TicketMaster;
import com.tutorial.repo.TicketMasterRepository;
import com.tutorial.service.EventPricingService;
import com.tutorial.service.EventService;
import com.tutorial.service.TicketDetailsService;
import com.tutorial.service.TicketMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class TicketMasterController {

    @Autowired
    private TicketMasterService ticketMasterService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventPricingService eventPricingService;
    @Autowired
    private TicketDetailsService ticketDetailsService;
    @Autowired
    private TicketMasterRepository ticketMasterRepository;

    @GetMapping("/tickets/new/{eventId}")
    public String showTicketForm(Model model, HttpSession session, @PathVariable String eventId) {
        Event event = eventService.findEventById(eventId);

        List<EventPricing> eventPricings = eventPricingService.findEventPricingsByEventId(eventId);
//        String ticketMasterId = ticketMaster.getTicketMasterId();
//
//        model.addAttribute("ticketMasterId", ticketMasterId);
        model.addAttribute("eventPricings", eventPricings);
        model.addAttribute("event", event);

        return "/ticket";
    }

//    @GetMapping("/tickets/new/{eventId}/{ticketId}")
//    public String showTicketFormAlso(Model model, HttpSession session, @PathVariable String eventId, @PathVariable String ticketId) {
//        Event event = eventService.findEventById(eventId);
//
//        TicketMaster ticketMaster = ticketMasterService.findTicketMasterById(ticketId);
//
////        String ticketMasterId = ticketMaster.getTicketMasterId();
////
////        model.addAttribute("ticketMasterId", ticketMasterId);
//        model.addAttribute("ticketMaster", ticketMaster);
//        model.addAttribute("event", event);
//
//        List<EventPricing> pricings = eventPricingService.findEventPricingsByEventId(eventId);
//        model.addAttribute("pricings", pricings);
//
//        List<TicketDetails> ticketDetails = ticketDetailsService.findTicketDetailsByTicketId(ticketMaster.getTicketMasterId());
//        model.addAttribute("ticketDetails", ticketDetails);
//
//        return "/ticket";
//    }


    @PostMapping("/add_ticket")
    public String handleTicketSubmission(
            @RequestParam String eventInput,
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam Map<String, String> params,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        TicketMaster ticketMaster = new TicketMaster();
        ticketMaster.setTicketMasterId(UUID.randomUUID().toString().split("-")[0]);
        ticketMaster.setFullName(fullName);
        ticketMaster.setEmail(email);
        ticketMaster.setEventId(eventInput);
        ticketMaster.setDateBought(new Date().toString());
        ticketMaster.setPaymentReceived(0);
        ticketMaster.setPaymentReceivedAt("null");
        ticketMaster.setPaymentReceivedBy("null");

        ticketMasterService.addTicketMaster(ticketMaster);
        double price = 0;
        int tickets = 0;

        for(Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();


            if(key.startsWith("pricing_")) {
                String pricingId = key.substring("pricing_".length());
                int numberOfTickets = 0;
                if(!Objects.equals(value, ""))
                {
                    numberOfTickets = Integer.parseInt(value);
                }

                for (int i = 0; i < numberOfTickets; i++) {
                    TicketDetails ticketDetails = new TicketDetails();
                    ticketDetails.setFullName(ticketMaster.getFullName());
                    ticketDetails.setTicketId(ticketMaster.getTicketMasterId());
                    ticketDetails.setDetailId(UUID.randomUUID().toString().split("-")[0]);
                    ticketDetails.setPricingOptionId(pricingId);
                    EventPricing pricing = eventPricingService.findEventPricingById(pricingId);
                    ticketDetails.setPricingOptionName(pricing.getPricingName());
                    ticketDetails.setAmount(pricing.getPricingRate());
                    ticketDetails.setCheckedIn(0);
                    ticketDetails.setCheckedInAt("null");
                    ticketDetails.setCheckedInBy("null");
                    ticketDetails.setUniqueCode(String.valueOf(getRandomCode()));
                    ticketDetails.setTicketBought(ticketMaster.getDateBought());
                    ticketDetails.setPaidStatus(0);
                    price += eventPricingService.findEventPricingById(ticketDetails.getPricingOptionId()).getPricingRate();
                    tickets += 1;
                    ticketDetailsService.addTicketDetails(ticketDetails);
                }
            }
        }

        ticketMaster.setTotalAmount(price);
        ticketMaster.setTotalTickets(tickets);

        ticketMasterService.updateTicketMaster(ticketMaster);

        Event event = eventService.findEventById(eventInput);

        redirectAttributes.addFlashAttribute("message", event.getNotesOnTickets());


        return "redirect:/dashboard";

    }


    @GetMapping("/tickets/confirmation")
    public String showConfirmation(Model model) {
        return "ticket_confirmation";  // Assuming you have a confirmation page
    }

    @GetMapping("/tickets/{ticketId}")
    public String getTicketById(@PathVariable String ticketId, Model model) {
        Optional<TicketMaster> ticketMaster = Optional.ofNullable(ticketMasterService.findTicketMasterById(ticketId));
        if (ticketMaster.isPresent()) {
            model.addAttribute("ticketMaster", ticketMaster.get());
            return "ticket_detail";
        } else {
            model.addAttribute("error", "Ticket not found");
            return "error";
        }
    }

    @GetMapping("/tickets")
    public String getAllTickets(Model model) {
        List<TicketMaster> tickets = ticketMasterService.findAllTicketMasters();
        model.addAttribute("tickets", tickets);
        return "ticket_list";  // Assuming you have a page to list all tickets
    }

    @GetMapping("/tickets/delete/{ticketId}")
    public String deleteTicket(@PathVariable String ticketId, Model model) {
        ticketMasterService.deleteTicketMaster(ticketId);
        return "redirect:/tickets";
    }

    public static int getRandomCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return number;
    }
}
