package com.phoenixplaydev.musicapp.configuration.security;

import com.phoenixplaydev.musicapp.model.tables.pojos.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Component
public class CredentialsInput implements UserDetails {
    private String email;
    private String password;

    private String role;

    public CredentialsInput() {}

    public CredentialsInput(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public CredentialsInput(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole().toString();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role.equals("User")) {
            return Set.of(new SimpleGrantedAuthority(role));
        }
        return Set.of(new GrantedAuthority[]{
                new SimpleGrantedAuthority("User"),
                new SimpleGrantedAuthority("Admin")
        });
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}