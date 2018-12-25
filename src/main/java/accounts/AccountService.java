package accounts;

import database.DBException;

import java.util.Collection;

/**
 * Interface for managing user accounts and their sessions.
 * @autor Alex
 */
public interface AccountService {

    void addNewUser(String login, String password) throws DBException;

    void addNewUser(String login, String password, UserAccount.Role role) throws DBException;

    void deleteUser(String id) throws DBException;

    void updateUser(UserAccount user) throws DBException;

    UserAccount getUserByLogin(String login) throws DBException;

    UserAccount getUserById(String id) throws DBException;

    UserAccount getUserBySessionId(String sessionId);

    void addSession(String sessionId, UserAccount userAccount);

    void deleteSession(String sessionId);

    Collection<UserAccount> getAllUsers() throws DBException;
}
