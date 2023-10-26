package com.phoenixplaydev.musicapp.service;

import com.phoenixplaydev.musicapp.model.enums.UserRole;
import com.phoenixplaydev.musicapp.model.tables.pojos.*;
import com.phoenixplaydev.musicapp.repository.*;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.NoDataFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        if(userRepository.isUserNotExists(email))
            throw new NoDataFoundException("User with email = " + email + " not found!");
        return userRepository.getUserByEmail(email);
    }

    public User getUserByID(Long userID) {
        if(userRepository.isUserNotExists(userID))
            throw new NoDataFoundException("User with ID = " + userID + " not found!");
        return userRepository.getUserByID(userID);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public void updateUserInfo(Long userID, String role,
                               String nickname, String email, String password) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't update non-existing user with ID = " + userID + "!");
        User user = userRepository.getUserByID(userID);
        if(role != null) {
            if(!Arrays.stream(UserRole.values()).map(UserRole::toString).toList().contains(role))
                throw new IllegalArgumentException("Can't find role = '" + role + "'!");
            user.setRole(UserRole.valueOf(role));
        }
        if(nickname != null) {
            if(nickname.isBlank())
                throw new IllegalArgumentException("User name can't be empty!");
            user.setNickname(nickname);
        }
        if(email != null) {
            if(email.isEmpty())
                throw new IllegalArgumentException("Email can't be empty!");
            user.setEmail(email);
        }
        if(password != null) {
            if(password.length() < 6 )
                throw new IllegalArgumentException("The password length can't be less than 6 letters!");
            user.setPassword(passwordEncoder.encode(password));
        }
        userRepository.updateUserInfo(user);
    }

    public void addUser(String role, String nickname, String email, String password) {
        User user = new User();
        if(role == null)
            throw new IllegalArgumentException("User role can't be null!");
        if(!Arrays.stream(UserRole.values()).map(UserRole::toString).toList().contains(role))
            throw new IllegalArgumentException("Can't find role = '" + role + "'!");
        user.setRole(UserRole.valueOf(role));
        if(nickname == null || nickname.isBlank())
            throw new IllegalArgumentException("User name can't be null or empty!");
        user.setNickname(nickname);
        if(email == null || email.isBlank())
            throw new IllegalArgumentException("User email can't be null or empty!");
        user.setEmail(email);
        if(password == null || password.isBlank())
            throw new IllegalArgumentException("Password can't be null or empty!");
        if(password.length() < 6)
            throw new IllegalArgumentException("password length can't be less than 6 letters!");
        user.setPassword(passwordEncoder.encode(password));
        userRepository.addUser(user);
    }

    public void updatePassword(Long userID, String oldPassword, String newPassword) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't update password of non-existing user with ID = " + userID);
        User user = userRepository.getUserByID(userID);

        if(!passwordEncoder.encode(oldPassword).equals(user.getPassword()))
            throw new AccessDeniedException("Can't update password! Entered wrong old password!");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.updateUserInfo(user);
    }

    public void deleteUser(Long userID) {
        if(userRepository.isUserNotExists(userID))
            throw new DataAccessException("Can't delete non-existing user with ID = " + userID + "!");
        userRepository.deleteUser(userID);
    }

}
