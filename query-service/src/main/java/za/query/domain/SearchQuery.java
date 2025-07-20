package za.query.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import za.shared.domain.DomainEvent;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class SearchQuery {
    private final String queryId;
    private final String userId;
    private final String rawQuery;
    private final String sanitizedQuery;
    private final QueryType queryType;
    private final Instant timestamp;
    private final String clientIpAddress;
    private final List<String> searchFilters;

    public SearchQuery(String userId, String rawQuery, QueryType queryType,
            String clientIpAddress, List<String> searchFilters) {
        this.queryId = UUID.randomUUID().toString();
        this.userId = userId;
        this.rawQuery = rawQuery;
        this.sanitizedQuery = sanitizeQuery(rawQuery);
        this.queryType = queryType;
        this.timestamp = Instant.now();
        this.clientIpAddress = clientIpAddress;
        this.searchFilters = searchFilters;
    }

    private String sanitizeQuery(String query) {
        // Implement SQL injection prevention and XSS protection
        return query.replaceAll("[<>\"'&]", "").trim();
    }

    // Getters
    public String getQueryId() {
        return queryId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRawQuery() {
        return rawQuery;
    }

    public String getSanitizedQuery() {
        return sanitizedQuery;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public List<String> getSearchFilters() {
        return searchFilters;
    }

    public static class QuerySubmittedEvent extends DomainEvent {
        private final String queryId;
        private final String sanitizedQuery;
        private final QueryType queryType;

        public QuerySubmittedEvent(String queryId, String sanitizedQuery, QueryType queryType) {
            super(queryId);
            this.queryId = queryId;
            this.sanitizedQuery = sanitizedQuery;
            this.queryType = queryType;
        }

        public String getQueryId() {
            return queryId;
        }

        public String getSanitizedQuery() {
            return sanitizedQuery;
        }

        public QueryType getQueryType() {
            return queryType;
        }
    }
}
