package com.eventflow.eventManagement.auth.controller.controller;

import com.eventflow.eventManagement.auth.service.UserService;
import com.eventflow.eventManagement.common.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
