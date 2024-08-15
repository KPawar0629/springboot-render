package com.tutorial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String eventLocation;
    private String eventDateTime;
    private String eventCreatedBy;
    private String eventCreatedAt;
    private String notesOnTickets;
    private String lastModifiedBy;
    private String lastModifiedAt;
}
