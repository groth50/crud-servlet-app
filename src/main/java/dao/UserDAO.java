package dao;

import accounts.UserAccount;
import database.DBException;

import java.util.Collection;

public interface UserDAO {
    UserAccount getUserById(long id) throws DBException;

    UserAccount getUserByLogin(String name) throws DBException;

    Collection<UserAccount> getAllUsers() throws DBException;

    long insertUser(String name, String password, String role) throws DBException;

    void deleteUser(String id) throws DBException;

    void updateUser(UserAccount user) throws DBException;
}