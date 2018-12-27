package accounts;

import database.DBException;
import database.DBService;
import java.util.*;

/**
 * Class for managing user accounts in database
 * and their sessions in application.
 *
 * @autor  Alex
 */
public class AccountServiceDB implements AccountService {

    /** Database layer, used for worked with database */
    private final DBService dbService;

    /** Keep maps the user http session {@link javax.servlet.http.HttpSession#toString()} to {@link UserAccount} */
    private final Map<String, UserAccount> sessionIdToProfile;

    // Constructs for tests
    public AccountServiceDB() {
        dbService = new DBService();
        sessionIdToProfile = Collections.synchronizedMap(new WeakHashMap<>());
    }

    /**
     * Constructs a new AccountServiceDB and initializes
     * an {@link DBService} and session store
     *
     * @param dbService DBService instance
     */
    public AccountServiceDB(DBService dbService) {
        this.dbService = dbService;
        // synchronized and if user don't logout, GC can to collect invalid session references
        // if you need more efficient, use third party libraries
        sessionIdToProfile = Collections.synchronizedMap(new WeakHashMap<>());
    }

    /**
     * It's wrapper for {@link DBService#getUserById(long)}
     */
    @Override
    public UserAccount getUserById(String id) throws DBException {
        return dbService.getUserById(Long.parseLong(id));
    }

    /**
     * It's wrapper for {@link DBService#getUserByLogin(String)}
     */
    @Override
    public UserAccount getUserByLogin(String login) throws DBException {
        return dbService.getUserByLogin(login);
    }

    /**
     * It's wrapper for {@link DBService#getAllUsers()}
     */
    @Override
    public Collection<UserAccount> getAllUsers() throws DBException {
        return dbService.getAllUsers();
    }

    /**
     * It's wrapper for {@link DBService#addNewUser(String, String, UserAccount.Role)},
     * but this method use default {@link UserAccount.Role#USER}
     */
    @Override
    public void addNewUser(String login, String password) throws DBException {
        UserAccount.Role roleDefault = UserAccount.Role.USER;
        dbService.addNewUser(login, password, roleDefault);
    }

    /**
     * It's wrapper for {@link DBService#addNewUser(String, String, UserAccount.Role)}
     */
    @Override
    public void addNewUser(String login, String password, UserAccount.Role role) throws DBException {
        dbService.addNewUser(login, password, role);
    }

    /**
     * It's wrapper for {@link DBService#deleteUser(String)}
     */
    @Override
    public void deleteUser(String id) throws DBException {
        dbService.deleteUser(id);
    }

    /**
     * It's wrapper for {@link DBService#updateUser(UserAccount)}
     */
    @Override
    public void updateUser(UserAccount user) throws DBException {
        dbService.updateUser(user);
    }

    /**
     * Return {@link UserAccount} by session ID
     *
     * @param sessionId  http session is <code>String</code>
     *                   {@link javax.servlet.http.HttpSession#toString()}
     *
     * @return UserAccount or <code>null</code>
     */
    @Override
    public UserAccount getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    /**
     * Add http session {@link javax.servlet.http.HttpSession#toString()}
     *
     * @param sessionId http session is <code>String</code>
     *                  {@link javax.servlet.http.HttpSession#toString()}
     *
     * @param userAccount {@link UserAccount}
     */
    @Override
    public void addSession(String sessionId, UserAccount userAccount) {
        sessionIdToProfile.put(sessionId, userAccount);
    }

    /**
     * Delete http session {@link javax.servlet.http.HttpSession#toString()}
     *
     * @param sessionId http session is <code>String</code>
     *                  {@link javax.servlet.http.HttpSession#toString()}
     */
    @Override
    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
