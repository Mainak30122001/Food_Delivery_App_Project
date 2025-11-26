package com.example.food_delivery_backend.service;

import com.example.food_delivery_backend.dto.LoginRequest;
import com.example.food_delivery_backend.dto.LoginResponse;
import com.example.food_delivery_backend.dto.RegisterRequest;
import com.example.food_delivery_backend.model.User;
import com.example.food_delivery_backend.Repository.UserRepository;
import com.example.food_delivery_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public String register(RegisterRequest request) {

        User user = User.builder()
                .name(request.getName())
                .username(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        repo.save(user);

        return "User Registered";
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Fetch user
        User user = repo.findByUsername(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Prepare JWT claims
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        claims.put("role", user.getRole());

        // Generate token
        String token = jwtService.generateToken(claims, user.getUsername());

        // Build LoginResponse
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setName(user.getName());
        response.setRole(user.getRole());

        return response;
    }

    @Override
    public User getUserByName(String name) {
        return repo.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("User not found with name: " + name));
    }

}
