package com.tutorial.service;

import com.tutorial.model.Event;
import com.tutorial.model.EventPricing;
import com.tutorial.model.User;
import com.tutorial.repo.EventPricingRepository;
import com.tutorial.repo.EventRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventPricingRepository pricingRepository;

    public Event createOrUpdateEvent(Event event, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (event.getEventId() != null && !event.getEventId().isEmpty()) {
            Optional<Event> existingEvent = eventRepository.findById(event.getEventId());
            if (existingEvent.isPresent()) {
                Event updatedEvent = existingEvent.get();
                updatedEvent.setEventName(event.getEventName());
                updatedEvent.setEventDescription(event.getEventDescription());
                updatedEvent.setEventLocation(event.getEventLocation());
                updatedEvent.setEventDateTime(event.getEventDateTime());
                updatedEvent.setNotesOnTickets(event.getNotesOnTickets());
                updatedEvent.setLastModifiedBy(loggedInUser.getUserId());
                updatedEvent.setLastModifiedAt(new Date().toString());

                return eventRepository.save(updatedEvent);
            }
        }
        return eventRepository.save(event);
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public Event findEventById(String id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Optional<Event> findEventByName(String name) {
        return eventRepository.findByEventName(name).stream().findFirst();
    }

    public Event updateEvent(Event event) {
        Event existingEvent = eventRepository.findById(event.getEventId()).get();
        existingEvent.setEventName(event.getEventName());
        existingEvent.setEventDescription(event.getEventDescription());
        existingEvent.setEventLocation(event.getEventLocation());
        existingEvent.setEventDateTime(event.getEventDateTime());
        existingEvent.setNotesOnTickets(event.getNotesOnTickets());

        return eventRepository.save(existingEvent);
    }

    public String deleteEvent(String id) {
        eventRepository.deleteById(id);
        return "Event deleted";
    }

    public long eventCount() {
        return eventRepository.count();
    }

    public List<Event> findEventsByName(String eventName) {
        return eventRepository.findByEventName(eventName);
    }

    public List<EventPricing> findEventPricingByEventId(String eventId) {
        return pricingRepository.findByEventId(eventId);
    }
}
