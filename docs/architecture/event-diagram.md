# Event Diagram - AI Search Engine

## Event Flow Overview

This diagram illustrates the sequence of events and interactions between microservices when processing a user query in the AI Search Engine platform.

```mermaid
sequenceDiagram
    participant User
    participant Gateway as Gateway Service
    participant Auth as Auth Service
    participant Query as Query Service
    participant Search as Search Service
    participant Data as Data Service
    participant Notify as Notification Service
    participant Config as Config Service
    participant Discovery as Discovery Service
    participant DB as PostgreSQL
    participant MessageBroker as RabbitMQ/Kafka

    %% Initial Setup and Service Discovery
    Discovery->>Config: Register service
    Config->>Discovery: Provide configuration
    Discovery->>Auth: Register service
    Config->>Auth: Provide auth configuration
    Discovery->>Query: Register service
    Config->>Query: Provide query configuration
    Discovery->>Search: Register service
    Config->>Search: Provide search configuration
    Discovery->>Data: Register service
    Config->>Data: Provide data configuration
    Discovery->>Notify: Register service
    Config->>Notify: Provide notification configuration

    %% User Query Flow
    User->>Gateway: POST /api/v1/query {query, token}
    Gateway->>Auth: Validate JWT token
    Auth->>Data: Verify user session
    Data-->>Auth: User session valid
    Auth-->>Gateway: Token valid (200 OK)
    
    Gateway->>Query: Forward query request
    Query->>Data: Log query for audit
    Data->>DB: Insert audit log
    DB-->>Data: Log stored
    Data-->>Query: Audit logged
    
    Query->>Search: Process search request
    Search->>Data: Retrieve search context
    Data->>DB: Query user preferences/history
    DB-->>Data: Return context data
    Data-->>Search: Context provided
    
    Search->>Search: Execute AI search (mocked)
    Search->>Data: Store search results
    Data->>DB: Insert search results
    DB-->>Data: Results stored
    Data-->>Search: Storage confirmed
    
    Search-->>Query: Search results with sources
    Query->>Data: Cache response
    Data->>DB: Update cache
    DB-->>Data: Cache updated
    Data-->>Query: Cache confirmed
    
    Query-->>Gateway: Formatted response
    Gateway-->>User: JSON response {answer, sources}

    %% Asynchronous Notification Flow
    Query->>MessageBroker: Publish query_completed event
    MessageBroker->>Notify: Deliver event
    Notify->>Data: Get user notification preferences
    Data->>DB: Query preferences
    DB-->>Data: Return preferences
    Data-->>Notify: Preferences provided
    
    Notify->>Notify: Format notification
    Notify->>MessageBroker: Publish notification_sent event
    MessageBroker-->>Notify: Event acknowledged
    
    %% Error Handling Flow
    rect rgb(255, 220, 220)
        alt Authentication Failure
            Gateway->>Auth: Validate JWT token
            Auth-->>Gateway: Invalid token (401 Unauthorized)
            Gateway-->>User: 401 Unauthorized
        end
        
        alt Rate Limit Exceeded
            Gateway->>Gateway: Check rate limits
            Gateway-->>User: 429 Too Many Requests
        end
        
        alt Service Unavailable
            Query->>Search: Process search request
            Search-->>Query: Service unavailable (503)
            Query->>Data: Log error
            Data->>DB: Insert error log
            DB-->>Data: Error logged
            Query-->>Gateway: 503 Service Unavailable
            Gateway-->>User: 503 Service Unavailable
        end
    end

    %% Health Check Flow
    loop Every 30 seconds
        Discovery->>Auth: Health check
        Auth-->>Discovery: Healthy
        Discovery->>Query: Health check
        Query-->>Discovery: Healthy
        Discovery->>Search: Health check
        Search-->>Discovery: Healthy
        Discovery->>Data: Health check
        Data-->>Discovery: Healthy
        Discovery->>Notify: Health check
        Notify-->>Discovery: Healthy
    end
```

## Key Event Types

### 1. Service Registration Events
- `service_registered`: When a service registers with discovery
- `configuration_updated`: When service configuration changes

### 2. User Query Events
- `query_received`: When a user query is received
- `query_validated`: After authentication and validation
- `query_processed`: After search execution
- `query_completed`: After response generation

### 3. Data Events
- `audit_log_created`: When audit logs are created
- `search_results_stored`: When search results are persisted
- `cache_updated`: When response cache is updated

### 4. Notification Events
- `notification_triggered`: When notifications need to be sent
- `notification_sent`: After successful notification delivery

### 5. Security Events
- `authentication_success`: Successful user authentication
- `authentication_failure`: Failed authentication attempts
- `authorization_denied`: When access is denied
- `rate_limit_exceeded`: When rate limits are hit

## Event Flow Characteristics

### Synchronous Flow
- User query processing follows a synchronous request-response pattern
- Each service validates and processes before passing to the next
- Fail-fast approach with proper error handling

### Asynchronous Flow
- Notifications and background processing use message brokers
- Event-driven architecture for loose coupling
- Guaranteed delivery and retry mechanisms

### Security Flow
- Every service-to-service call is authenticated
- Zero-trust security model implemented
- Comprehensive audit logging for all events

### Monitoring Flow
- Health checks ensure service availability
- Metrics collection for performance monitoring
- Distributed tracing for request correlation
