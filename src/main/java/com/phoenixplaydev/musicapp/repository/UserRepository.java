package com.phoenixplaydev.musicapp.repository;

import com.phoenixplaydev.musicapp.model.Tables;
import com.phoenixplaydev.musicapp.model.tables.pojos.*;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private DSLContext dslContext;

    public boolean isUserNotExists(Long userID) {
        return !dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.ID.eq(userID))
                .fetch().isNotEmpty();
    }

    public boolean isUserNotExists(String email) {
        return !dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.EMAIL.eq(email))
                .fetch().isNotEmpty();
    }

    public User getUserByEmail(String email) {
        return dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.EMAIL.eq(email))
                .fetchOneInto(User.class);
    }

    public User getUserByID(Long userID) {
        return dslContext.selectFrom(Tables.USER)
                .where(Tables.USER.ID.eq(userID))
                .fetchOneInto(User.class);
    }

    public List<User> getAllUsers() {
        return dslContext.selectFrom(Tables.USER)
                .fetchInto(User.class);
    }

    public void addUser(User user) {
        dslContext.insertInto(
                    Tables.USER,
                    Tables.USER.ROLE,
                    Tables.USER.NICKNAME,
                    Tables.USER.EMAIL,
                    Tables.USER.PASSWORD
                ).values(
                    user.getRole(),
                    user.getNickname(),
                    user.getEmail(),
                    user.getPassword()
        ).execute();
    }

    public void updateUserInfo(User user) {
        dslContext.update(Tables.USER)
                .set(dslContext.newRecord(Tables.USER, user))
                .where(Tables.USER.ID.eq(user.getId()))
                .returning()
                .fetchOptional()
                .orElseThrow(() ->
                        new DataAccessException(
                                "Can't update user with ID = " +
                                        user.getId()))
                .into(User.class);
    }

    public void deleteUser(Long userID) {
        dslContext.deleteFrom(Tables.USER)
                .where(Tables.USER.ID.eq(userID)).execute();
    }

}
