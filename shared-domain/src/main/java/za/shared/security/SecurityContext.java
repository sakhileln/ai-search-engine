package za.shared.security;

import java.util.Set;

public class SecurityContext {
    private final String userId;
    private final String sessionId;
    private final Set<String> roles;
    private final Set<String> permissions;
    private final String clientIpAddress;
    private final boolean authenticated;

    public SecurityContext(String userId, String sessionId, Set<String> roles, 
                          Set<String> permissions, String clientIpAddress, boolean authenticated) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.roles = roles;
        this.permissions = permissions;
        this.clientIpAddress = clientIpAddress;
        this.authenticated = authenticated;
    }

    public String getUserId() { return userId; }
    public String getSessionId() { return sessionId; }
    public Set<String> getRoles() { return roles; }
    public Set<String> getPermissions() { return permissions; }
    public String getClientIpAddress() { return clientIpAddress; }
    public boolean isAuthenticated() { return authenticated; }
    
    public boolean hasRole(String role) { return roles.contains(role); }
    public boolean hasPermission(String permission) { return permissions.contains(permission); }
}
