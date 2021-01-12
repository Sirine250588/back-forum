package com.example.blog.blog.service;

import com.example.blog.blog.dto.LoginRequest;
import com.example.blog.blog.dto.RegisterRequest;
import com.example.blog.blog.model.User;
import com.example.blog.blog.repository.UserRepository;
import com.example.blog.blog.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;


    // La fonction signup !!

    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUserName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));

        userRepository.save(user);
    }


    // La fonction encodePassword <-> L'interface Standart PasswordEncoder.

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }


// Le type de retour la fonction AuthenticationResponse et les paramètre de type LoginRequest
    // La fonction Login

    public AuthenticationResponse login(LoginRequest loginRequest) {
        // authenticate de type Authentication <-> authenticationManager
        // UsernamePasswordAuthenticationToken.
        // SecurityContextHolder.<=> les classes standarts
        // AuthenticationResponse classe
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        // Appel de la classe jwtProvider <-> de la fonction generateToken
        String authenticationToken = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(authenticationToken, loginRequest.getUsername());
    }

// Lier "username" entre User et Post.
    public Optional<org.springframework.security.core.userdetails.User> getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return Optional.of(principal);
    }
}