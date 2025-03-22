package com.tomjerry.expensetracker.service;

import com.tomjerry.expensetracker.dto.AuthRequest;
import com.tomjerry.expensetracker.dto.AuthResponse;
import com.tomjerry.expensetracker.exceptions.InvalidCredentialsException;
import com.tomjerry.expensetracker.model.User;
import com.tomjerry.expensetracker.repository.UserRepository;
import com.tomjerry.expensetracker.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthResponse login(AuthRequest authRequest) {
        Optional<User> userOptional = userRepository.findByEmail(authRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user);
                return new AuthResponse(token);
            }
        }
        throw new InvalidCredentialsException("Invalid email or password");
    }
}