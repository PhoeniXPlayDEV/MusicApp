package com.phoenixplaydev.musicapp.service.authentication;

import com.phoenixplaydev.musicapp.configuration.security.CredentialsInput;
import com.phoenixplaydev.musicapp.configuration.security.JwtService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import jakarta.annotation.security.PermitAll;
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

    @PermitAll
    @GraphQLQuery(name = "signIn")
    public AuthenticationResponse signIn(@GraphQLArgument(name = "email") String email,
                                         @GraphQLArgument(name = "password") String password) {

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
