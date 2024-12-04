package org.example.security.authentication;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.model.Token;
import org.example.model.User;
import org.example.repository.TokenRepository;
import org.example.repository.UserRepository;
import org.example.security.jwt.JwtService;
import org.example.security.authentication.dto.AuthenticationRequest;
import org.example.security.authentication.dto.AuthenticationResponse;
import org.example.security.authentication.dto.RegisterRequest;
import org.example.utils.Role;
import org.example.utils.TokenType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = org.example.model.User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user, request.isRememberMe());
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User with username " + request.getUsername() + " not found"));
        var jwtToken = jwtService.generateToken(user, request.isRememberMe());
        saveUserToken(user, jwtToken);
        return new AuthenticationResponse(jwtToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}