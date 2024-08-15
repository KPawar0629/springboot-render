package com.tutorial.repo;

import com.tutorial.model.Event;
import com.tutorial.service.EventPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByEventName(String name);
}
