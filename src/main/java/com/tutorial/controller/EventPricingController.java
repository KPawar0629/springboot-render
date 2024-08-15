package com.tutorial.controller;

import com.tutorial.model.Event;
import com.tutorial.model.EventPricing;
import com.tutorial.model.User;
import com.tutorial.service.EventPricingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EventPricingController {
    @Autowired
    private EventPricingService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventPricing createEventPricing(@RequestBody EventPricing eventPricing) {
        return service.addEventPricing(eventPricing);
    }

    @PostMapping("/event_pricing")
    public String createOrUpdateEventPricing(
            @RequestParam String eventId,
            @RequestParam(required = false) String eventPricingId,
            @RequestParam String ticketType,
            @RequestParam String description,
            @RequestParam double ratePerTicket,
            Model model
    ) {
        EventPricing eventPricing;
        if (eventPricingId != null && !eventPricingId.isEmpty()) {
            eventPricing = service.findEventPricingById(eventPricingId);
            if (eventPricing == null) {
                model.addAttribute("error", "Pricing not found!");
                return "redirect:/events/edit/" + eventId;
            }
        } else {
            eventPricing = new EventPricing();
            eventPricing.setId(UUID.randomUUID().toString().split("-")[0]);
            eventPricing.setEventId(eventId);
        }

        eventPricing.setPricingName(ticketType);
        eventPricing.setPricingDesc(description);
        eventPricing.setPricingRate(ratePerTicket);
        service.addOrUpdateEventPricing(eventPricing);
        return "redirect:/events/edit/" + eventId;
    }

    @GetMapping("/event_pricing/delete/{eventPricingId}")
    public String deleteEventPricing(@PathVariable String eventPricingId, HttpSession session) {
        EventPricing eventPricing = service.findEventPricingById(eventPricingId);
        if (eventPricing != null) {
            service.deleteEventPricing(eventPricingId);
        }
        String eventId = eventPricing != null ? eventPricing.getEventId() : "";
        return "redirect:/events/edit/" + eventId;
    }

    @GetMapping("/eventpricing/{eventId}")
    public List<EventPricing> getAllEventPricingsByEventId(@PathVariable String eventId) {
        return service.findEventPricingsByEventId(eventId);
    }

    @GetMapping("/eventpricing/{eventPricingId}")
    public EventPricing getEventPricingById(@PathVariable String eventPricingId) {
        return service.findEventPricingById(eventPricingId);
    }

    @PutMapping("/eventpricing/update")
    public EventPricing updateEventPricing(@RequestBody EventPricing eventPricing) {
        return service.updateEventPricing(eventPricing);
    }

    @DeleteMapping("/{eventPricingId}")
    public String deleteEventPricing(@PathVariable String eventPricingId) {
        return service.deleteEventPricing(eventPricingId);
    }
}
