package za.auth.controller;

import za.auth.entity.User;
import za.auth.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        try {
            User user = authService.getUserByEmail(principal.getName());
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> updateCurrentUser(@RequestBody User userDetails, Principal principal) {
        try {
            User updatedUser = authService.updateUser(principal.getName(), userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/profile/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        try {
            User user = authService.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteCurrentUser(Principal principal) {
        try {
            authService.deleteUser(principal.getName());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = authService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
