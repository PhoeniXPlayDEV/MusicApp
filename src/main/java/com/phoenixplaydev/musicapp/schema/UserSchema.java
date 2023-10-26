package com.phoenixplaydev.musicapp.schema;

import com.phoenixplaydev.musicapp.configuration.security.RolesChecker;
import com.phoenixplaydev.musicapp.model.tables.pojos.*;
import com.phoenixplaydev.musicapp.service.UserService;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSchema {

    @Autowired
    private UserService userService;

    @Autowired
    private RolesChecker rolesChecker;

    @GraphQLQuery(name = "getUserByID")
    public User getUserByID(@GraphQLArgument(name = "user_id") Long userID) {
        rolesChecker.userHasAdminRole();
        return userService.getUserByID(userID);
    }

    @GraphQLQuery(name = "getAllUsers")
    public List<User> getAllUsers() {
        rolesChecker.userHasAdminRole();
        return userService.getAllUsers();
    }

    @GraphQLMutation(name = "addUser")
    public void addUser(@GraphQLArgument(name = "role") String role,
                        @GraphQLArgument(name = "nickname") String nickname,
                        @GraphQLArgument(name = "email") String email,
                        @GraphQLArgument(name = "password") String password) {
        rolesChecker.userHasAdminRole();
        userService.addUser(role, nickname, email, password);
    }

    @GraphQLMutation(name = "updateUserInfo")
    public void updateUserInfo(@GraphQLArgument(name = "user_id") Long userID,
                               @GraphQLArgument(name = "role") String role,
                               @GraphQLArgument(name = "nickname") String nickname,
                               @GraphQLArgument(name = "email") String email,
                               @GraphQLArgument(name = "password") String password
    ) {
        rolesChecker.userHasAdminRole();
        userService.updateUserInfo(userID, role, nickname, email, password);
    }

    @GraphQLMutation(name = "updatePassword")
    public void updatePassword(@GraphQLArgument(name = "old_password") String oldPassword,
                               @GraphQLArgument(name = "new_password") String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long requestersID = userService.getUserByEmail(authentication.getName()).getId();
        userService.updatePassword(requestersID, oldPassword, newPassword);
    }

    @GraphQLMutation(name = "deleteUser")
    public void deleteUser(@GraphQLArgument(name = "user_id") Long userID){
        rolesChecker.userHasAdminRole();
        userService.deleteUser(userID);
    }

}
