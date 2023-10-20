package com.phoenixplaydev.musicapp.configuration.security;

import com.phoenixplaydev.musicapp.model.tables.pojos.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Component
public class CredentialsInput implements UserDetails {

    @NonNull
    private String email;
    @NonNull
    private String password;

    private String role;

    public CredentialsInput(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role.equals("User"))
            return Set.of(new SimpleGrantedAuthority(role));
        return Set.of(new GrantedAuthority[]{new SimpleGrantedAuthority("User"), new SimpleGrantedAuthority("Admin")});
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