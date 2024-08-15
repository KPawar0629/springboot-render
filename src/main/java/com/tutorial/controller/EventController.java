package com.tutorial.controller;

import com.tutorial.model.Event;
import com.tutorial.model.EventPricing;
import com.tutorial.model.User;
import com.tutorial.service.EventPricingService;
import com.tutorial.service.EventService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
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
public class EventController {

    @Autowired
    private EventService service;
    @Autowired
    private EventPricingService pricingService;

    @GetMapping("/events/new")
    public String showCreateEventForm(Model model, HttpSession session) {
        model.addAttribute("event", new Event());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        if(session.getAttribute("loggedInUser") != null) {
            return "/event_form";
        } else {
            return "redirect:/signin";
        }

    }

    @GetMapping("/events/edit/{eventId}")
    public String showEditEventForm(@PathVariable String eventId, @RequestParam(required = false) String pricingId,Model model, HttpSession session) {
        Event event = service.findEventById(eventId);
        model.addAttribute("event", event);
        List<EventPricing> eventPricings = pricingService.findEventPricingsByEventId(event.getEventId());
        model.addAttribute("eventPricings", eventPricings);

        if(pricingId != null && !pricingId.isEmpty()) {
            EventPricing pricing = pricingService.findEventPricingById(pricingId);
            model.addAttribute("pricing", pricing);
        }
        if(session.getAttribute("loggedInUser") != null) {
            return "/event_form";
        } else {
            return "redirect:/signin";
        }
    }

    @PostMapping("/events")
    public String handleEventCreationOrUpdate(
            @RequestParam(required = false) String eventId,
            @RequestParam String eventName,
            @RequestParam String eventDescription,
            @RequestParam String eventLocation,
            @RequestParam String eventDateTime,
            @RequestParam String notesOnTickets,
            HttpSession session,
            Model model
    ) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        Event event;
        if (eventId != null && !eventId.isEmpty()) {
            event = service.findEventById(eventId);
            if(event == null) {
                model.addAttribute("error", "Event not found!");
                System.out.println("Event not found!");
                return "/event_list";
            }
        } else {
            Optional<Event> existingEvent = service.findEventByName(eventName);

            if (service.eventCount() >= 10) {
                model.addAttribute("error", "Too many events! Wait for some events to finish or delete some!");
                System.out.println("Too many events!");
                return "/event_list";
            }

            if (existingEvent.isPresent()) {
                model.addAttribute("error", "Event already exists!");
                return "/event_form";
            }

            event = new Event();
            event.setEventId(UUID.randomUUID().toString().split("-")[0]);
            event.setEventCreatedAt(new Date().toString());
            if (loggedInUser != null) {
                event.setEventCreatedBy(loggedInUser.getUserId());
            }
            event.setLastModifiedBy("null");
            event.setLastModifiedAt("null");
        }

        event.setEventName(eventName);
        event.setEventDescription(eventDescription);
        event.setEventLocation(eventLocation);
        event.setEventDateTime(eventDateTime);
        event.setNotesOnTickets(notesOnTickets);
        service.createOrUpdateEvent(event, session);
        System.out.println("Event created!");
        return "redirect:/event_list";
    }

    @GetMapping("/event_list")
    public String getAllEvents(Model model, HttpSession session) {
        List<Event> events = service.findAllEvents();
        model.addAttribute("events", events);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);  // Ensure this is added
        if(session.getAttribute("loggedInUser") != null) {
            return "/event_list";
        } else {
            return "redirect:/signin";
        }
    }

//    @GetMapping("/events/{eventId}")
//    public String getEventById(@PathVariable String eventId, Model model) {
//        Event foundEvent = service.findEventById(eventId);
//        model.addAttribute("event", foundEvent);
//        return("/event_detail/e" + foundEvent.getEventId());
//    }

    @GetMapping("/name/{eventName}")
    public String getEventsByName(@PathVariable String eventName, Model model) {
        List<Event> events = service.findEventsByName(eventName);
        model.addAttribute("events", events);
        return "/event_list";
    }

    @PutMapping
    public String updateEvent(@RequestBody Event event, Model model) {
        Event updatedEvent = service.updateEvent(event);
        model.addAttribute("event", updatedEvent);
        return "/event_detail";
    }

    @GetMapping("/events/delete/{eventId}")
    public String deleteEvent(@PathVariable String eventId, Model model, HttpSession session) {
        if(session.getAttribute("loggedInUser") != null) {
            String deletedEvent = service.deleteEvent(eventId);
            List<EventPricing> eventPricings = pricingService.findEventPricingsByEventId(eventId);
            for(EventPricing eventPricing : eventPricings) {
                pricingService.deleteEventPricing(eventPricing.getId());
            }
            model.addAttribute("message", deletedEvent);
            return "redirect:/event_list";
        } else {
            return "redirect:/dashboard";
        }

    }
}
