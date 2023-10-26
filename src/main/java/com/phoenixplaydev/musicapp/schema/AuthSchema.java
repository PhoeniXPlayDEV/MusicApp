package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.response.AuthenticationResponse;
import com.phoenixplaydev.musicapp.service.AuthService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthSchema {

    @Autowired
    private AuthService authService;

    @PermitAll
    @GraphQLQuery(name = "signIn")
    public AuthenticationResponse signIn(@GraphQLArgument(name = "email") String email,
                                         @GraphQLArgument(name = "password") String password) {
        return authService.signIn(email, password);
    }

}
