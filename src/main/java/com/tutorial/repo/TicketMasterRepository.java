package com.tutorial.repo;

import com.tutorial.model.EventPricing;
import com.tutorial.model.TicketMaster;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketMasterRepository extends MongoRepository<TicketMaster, String> {

    List<TicketMaster> findByEventId(String eventId);

    List<TicketMaster> findByEmail(String email);
}
