package edu.malaka96.authify_backend.service;

import edu.malaka96.authify_backend.service.impl.CustomerUserDetailsService;
import edu.malaka96.authify_backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomerUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public String login(String email, String password) {
        // Authenticate user
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // Load user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Generate JWT
        return jwtUtil.generateToken(userDetails);
    }
}

