package com.eventflow.eventManagement.auth.controller.controller;

import com.eventflow.eventManagement.auth.repository.UserRepository;
import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.dto.User;
import com.eventflow.eventManagement.incident.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody User user) {
        int id = repository.save(user);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<Optional<User>> getUserByUsername(@RequestParam String username) {
        Optional<User> user = repository.findByUsername(username);
        return ResponseEntity.ok(user);
    }
}