package com.cryptotracker.backend.controller;

import com.cryptotracker.backend.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    // 🔐 Protected test endpoint
    @GetMapping("/test")
    public String testAuth() {
        return "Authenticated API is working!";
    }

    // 🔥 IMPORTANT: Endpoint to return logged-in user details
    @GetMapping("/me")
    public User getLoggedUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
