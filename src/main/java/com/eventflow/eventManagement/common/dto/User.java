package com.eventflow.eventManagement.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String email;

    private String passwordHash;

    private String role;

    private LocalDateTime createdAt = LocalDateTime.now();
}