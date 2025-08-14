package za.shared.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    @JsonProperty("eventId")
    private final String eventId;
    
    @JsonProperty("aggregateId")
    private final String aggregateId;
    
    @JsonProperty("occurredOn")
    private final Instant occurredOn;
    
    @JsonProperty("eventType")
    private final String eventType;

    protected DomainEvent(String aggregateId) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.occurredOn = Instant.now();
        this.eventType = this.getClass().getSimpleName();
    }

    public String getEventId() { return eventId; }
    public String getAggregateId() { return aggregateId; }
    public Instant getOccurredOn() { return occurredOn; }
    public String getEventType() { return eventType; }
}
