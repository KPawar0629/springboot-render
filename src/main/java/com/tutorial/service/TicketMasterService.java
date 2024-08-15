package com.tutorial.service;

import com.tutorial.model.EventPricing;
import com.tutorial.model.TicketMaster;
import com.tutorial.model.User;
import com.tutorial.repo.EventPricingRepository;
import com.tutorial.repo.EventRepository;
import com.tutorial.repo.TicketMasterRepository;
import com.tutorial.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TicketMasterService {
    @Autowired
    private TicketMasterRepository repository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventPricingRepository pricingRepository;
    @Autowired
    private UserRepository userRepository;

    public TicketMaster addTicketMaster(TicketMaster ticketMaster) {
        return repository.save(ticketMaster);
    }

    public TicketMaster findTicketMasterById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No TicketMaster found with id: " + id));
    }


    public List<TicketMaster> findTicketMasterByEventId(String eventId) {
        return repository.findByEventId(eventId);
    }

    public List<TicketMaster> findTicketMasterByEmail(String email) {
        return repository.findByEmail(email);
    }

    public TicketMaster updateTicketMaster(TicketMaster ticketMaster) {
        return repository.save(ticketMaster);
    }

    public void deleteTicketMaster(String id) {
        repository.deleteById(id);
    }

    public long ticketMasterCountPerEvent(String eventId) {
        return repository.findByEventId(eventId).size();
    }

    public List<TicketMaster> findAllTicketMasters() {
        return repository.findAll();
    }

    public String getPaymentRecieverNameById(String id) {
        User user = userRepository.findById(id).get();
        return user.getFullName();
    }
}
