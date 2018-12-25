package dao;

import accounts.UserAccount;

import java.util.Collection;

public interface UserDAO {
    UserAccount getUserById(long id);

    UserAccount getUserByLogin(String login);

    Collection<UserAccount> getAllUsers();

    void insertUser(String name, String password, String role);

    void deleteUser(String id);

    void updateUser(UserAccount user);
}