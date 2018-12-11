package dao;

import accounts.UserAccount;
import database.DBException;

import java.util.Collection;

public interface UserDAO {
    UserAccount getUserById(long id) throws DBException;

    UserAccount getUserByLogin(String name) throws DBException;

    Collection<UserAccount> getAllUsers() throws DBException;

    int insertUser(String name, String password, String role) throws DBException;

    int deleteUser(String id) throws DBException;

    int updateUser(UserAccount user) throws DBException;

    int createTable() throws DBException;

    int dropTable() throws DBException;
}