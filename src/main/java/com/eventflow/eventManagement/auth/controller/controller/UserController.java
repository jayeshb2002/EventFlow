package com.eventflow.eventManagement.auth.controller.controller;

import com.eventflow.eventManagement.auth.repository.UserRepository;
import com.eventflow.eventManagement.auth.service.UserService;
import com.eventflow.eventManagement.common.dto.Incident;
import com.eventflow.eventManagement.common.dto.RegisterRequest;
import com.eventflow.eventManagement.common.dto.User;
import com.eventflow.eventManagement.incident.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegisterRequest req) {
        Long id = service.register(
                req.getUsername(),
                req.getEmail(),
                req.getPassword(),
                req.getRole()
        );
        return ResponseEntity.ok(id);
    }
}
