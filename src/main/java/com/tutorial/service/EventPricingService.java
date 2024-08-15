package com.tutorial.service;

import com.tutorial.model.EventPricing;
import com.tutorial.repo.EventPricingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventPricingService {
    @Autowired
    private EventPricingRepository repository;

    public EventPricing addEventPricing(EventPricing eventPricing) {
        return repository.save(eventPricing);
    }

    public EventPricing findEventPricingById(String id) {
        return repository.findById(id).get();
    }

    public List<EventPricing> findEventPricingsByEventId(String eventId) {
        return repository.findByEventId(eventId);
    }

    public EventPricing updateEventPricing(EventPricing eventPricing) {
        EventPricing existingPricing = repository.findById(eventPricing.getId()).get();
        existingPricing.setPricingName(eventPricing.getPricingName());
        existingPricing.setPricingRate(eventPricing.getPricingRate());
        existingPricing.setPricingDesc(eventPricing.getPricingDesc());
        return repository.save(existingPricing);
    }

    public String deleteEventPricing(String id) {
        repository.deleteById(id);
        return "Event Pricing deleted";
    }

    public long eventPricingCountPerEvent(String eventId) {
        return repository.findByEventId(eventId).size();
    }

    public EventPricing addOrUpdateEventPricing(EventPricing eventPricing) {
        if(eventPricing.getId() != null && !eventPricing.getId().isEmpty()) {
            Optional<EventPricing> existingEventPricing = repository.findById(eventPricing.getId());
            if(existingEventPricing.isPresent()) {
                EventPricing updatedPricing = existingEventPricing.get();
                updatedPricing.setPricingName(eventPricing.getPricingName());
                updatedPricing.setPricingRate(eventPricing.getPricingRate());
                updatedPricing.setPricingDesc(eventPricing.getPricingDesc());
                return repository.save(updatedPricing);
            }
        }
        return repository.save(eventPricing);
    }
}
