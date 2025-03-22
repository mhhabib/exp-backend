package com.tomjerry.expensetracker.controller;

import com.tomjerry.expensetracker.dto.AuthRequest;
import com.tomjerry.expensetracker.dto.AuthResponse;
import com.tomjerry.expensetracker.dto.UserInfoDTO;
import com.tomjerry.expensetracker.model.User;
import com.tomjerry.expensetracker.repository.UserRepository;
import com.tomjerry.expensetracker.security.JwtUtil;
import com.tomjerry.expensetracker.service.AuthService;
import com.tomjerry.expensetracker.service.UserService;
import com.tomjerry.expensetracker.exceptions.InvalidCredentialsException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")  // Enable CORS for all methods in this controller
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody User user) {
        try {

            User createdUser = userService.signup(user);
            String token = jwtUtil.generateToken(user);
            AuthResponse authResponse = new AuthResponse(token);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            // Log and return error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse authResponse = authService.login(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/me/{email}")
    public ResponseEntity<UserInfoDTO> getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
                .map(user -> ResponseEntity.ok(new UserInfoDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
