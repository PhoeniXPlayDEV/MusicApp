package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.configuration.security.CredentialsInput;
import com.phoenixplaydev.musicapp.configuration.security.JwtService;
import com.phoenixplaydev.musicapp.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public AuthenticationResponse signIn(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        CredentialsInput credentials = new CredentialsInput(email, password);
        var jwtToken = jwtService.generateToken(credentials);
        return new AuthenticationResponse(jwtToken);
    }

}
