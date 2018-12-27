package accounts;

import database.DBException;
import java.util.Collection;

/**
 * Interface for managing user accounts
 * and their sessions in application.
 *
 * @autor  Alex
 */
public interface AccountService {

    /**
     * Return {@link UserAccount} entity by ID from database
     *
     * @param id {@link UserAccount#id} is user ID in application
     *
     * @return UserAccount entity from database
     *
     * @throws DBException if the query for database fails
     */
    UserAccount getUserById(String id) throws DBException;

    /**
     * Return {@link UserAccount} entity by login from database
     *
     * @param login {@link UserAccount#login}
     *              is user login in application
     *
     * @return {@link UserAccount} entity from database
     *
     * @throws DBException if the query for database fails
     */
    UserAccount getUserByLogin(String login) throws DBException;

    /**
     *
     * Returns a {@link Collection} contains all
     * {@link UserAccount} entities from database
     *
     * @return Collection contains all UserAccount
     *         entities from database
     *
     * @throws DBException if the query for database fails
     */
    Collection<UserAccount> getAllUsers() throws DBException;

    /**
     * Adds new {@link UserAccount} to database
     * use default {@link UserAccount.Role#USER}
     *
     * @param login {@link UserAccount#login}
     *              is user login in application
     *
     * @param password {@link UserAccount#password}
     *                 is user password in application
     *
     * @throws DBException if the query for database fails
     */
    void addNewUser(String login, String password) throws DBException;

    /**
     * Adds new {@link UserAccount} to database
     *
     * @param login {@link UserAccount#login}
     *              is user login in application
     *
     * @param password {@link UserAccount#password}
     *                 is user password in application
     *
     * @param role {@link UserAccount#role}
     *             is user role in application
     *
     * @throws DBException if the query for database fails
     */
    void addNewUser(String login, String password, UserAccount.Role role) throws DBException;

    /**
     * Delete {@link UserAccount} from database by ID
     *
     * @param id {@link UserAccount#id}
     *           is user ID in application to be removed
     *
     * @throws DBException if the query for database fails
     */
    void deleteUser(String id) throws DBException;

    /**
     * Save the changed {@link UserAccount} to database in ORM style
     *
     * @param user updateable UserAccount
     *
     * @throws DBException if the query for database fails
     */
    void updateUser(UserAccount user) throws DBException;

    /**
     * Return {@link UserAccount} by session ID
     *
     * @param sessionId  http session is <code>String</code>
     *                   {@link javax.servlet.http.HttpSession#toString()}
     *
     * @return UserAccount or <code>null</code>
     */
    UserAccount getUserBySessionId(String sessionId);

    /**
     * Add http session {@link javax.servlet.http.HttpSession#toString()}
     *
     * @param sessionId http session is <code>String</code>
     *                  {@link javax.servlet.http.HttpSession#toString()}
     *
     * @param userAccount {@link UserAccount}
     */
    void addSession(String sessionId, UserAccount userAccount);

    /**
     * Delete http session {@link javax.servlet.http.HttpSession#toString()}
     *
     * @param sessionId http session is <code>String</code>
     *                  {@link javax.servlet.http.HttpSession#toString()}
     */
    void deleteSession(String sessionId);
}
