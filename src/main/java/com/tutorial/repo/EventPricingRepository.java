package com.tutorial.repo;

import com.tutorial.model.EventPricing;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventPricingRepository extends MongoRepository<EventPricing, String> {
    List<EventPricing> findByEventId(String eventId);
}
