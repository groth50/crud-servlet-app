package database;

import accounts.UserAccount;
import dao.UserDAO;
import dao.UserDAOI;
import java.util.Collection;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The class is used to work with the database.
 * Provide initialization and configuration application
 * for working with a database and exception handling.
 *
 * @autor  Alex
 */
public class DBService {

    /** Standard logger */
    private static final Logger LOGGER = LogManager.getLogger(DBService.class.getName());

    /** Default named persistence unit */
    private static final String PERSISTENCE_UNIT_NAME = "PERSISTENCE";

    /** Default error message */
    private static final String DEFAULT_MESSAGE = "Sorry, we have trouble with server. Try again.";

    /** Data access object for users tables */
    private final UserDAO userDAO;

    /**
     * Constructs a new DBService and initializes
     * an {@link UserDAO} using {@link EntityManagerFactory}
     * from {@link DBService#createEntityManagerFactory}
     */
    public DBService() {
        userDAO = new UserDAOI(createEntityManagerFactory());
    }

    /**
     * Return {@link UserAccount} entity by ID from database
     *
     * @param id {@link UserAccount#id} is user ID in application
     *
     * @return UserAccount entity from database
     *
     * @throws DBException if the query for database fails
     */
    public UserAccount getUserById(long id) throws DBException {
        UserAccount userAccount = null;
        try {
            userAccount = userDAO.getUserById(id);
        } catch (NoResultException e) {
            LOGGER.error(e);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(DEFAULT_MESSAGE, e);
        }
        return userAccount;
    }

    /**
     * Return {@link UserAccount} entity by login from database
     *
     * @param login {@link UserAccount#login}
     * is user login in application
     *
     * @return {@link UserAccount} entity from database
     *
     * @throws DBException if the query for database fails
     */
    public UserAccount getUserByLogin(String login) throws DBException {
        UserAccount userAccount = null;
        try {
            userAccount = userDAO.getUserByLogin(login);
        } catch (NoResultException e) {
            LOGGER.error(e);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(DEFAULT_MESSAGE, e);
        }
        return userAccount;
    }

    /**
     * Add new {@link UserAccount} in database
     *
     * @param login {@link UserAccount#login}
     * is user login in application
     *
     * @param password {@link UserAccount#password}
     * is user password in application
     *
     * @param role {@link UserAccount#role}
     * is user role in application
     *
     * @throws DBException if the query for database fails
     */
    public void addNewUser(String login, String password, UserAccount.Role role) throws DBException {
        try {
            userDAO.insertUser(login, password, role.toString());
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(DEFAULT_MESSAGE, e);
        }
    }

    /**
     * Delete {@link UserAccount} from database by ID
     *
     * @param id {@link UserAccount#id}
     * is user ID in application to be removed
     *
     * @throws DBException if the query for database fails
     */
    public void deleteUser(String id) throws DBException {
        try {
            userDAO.deleteUser(id);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(DEFAULT_MESSAGE, e);
        }
    }

    /**
     * Update {@link UserAccount} in database in ORM style,
     * user is updated UserAccount
     *
     * @param user updateable UserAccount
     *
     * @throws DBException if the query for database fails
     */
    public void updateUser(UserAccount user) throws DBException {
        try {
            userDAO.updateUser(user);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(DEFAULT_MESSAGE, e);
        }
    }

    /**
     *
     * Returns a {@link Collection} contains all
     * {@link UserAccount} entities from database
     *
     * @return Collection contains all UserAccount
     * entities from database
     *
     * @throws DBException if the query for database fails
     */
    public Collection<UserAccount> getAllUsers() throws DBException {
        Collection<UserAccount> allUsers = null;
        try {
            allUsers = userDAO.getAllUsers();
        } catch (NoResultException e) {
            LOGGER.error(e);
            throw new DBException("Can't find user", e);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(DEFAULT_MESSAGE, e);
        }
        if (allUsers == null || allUsers.isEmpty()) {
            throw new DBException("Can't find user");
        }
        return allUsers;
    }

    /**
     * Create and return an {@link EntityManagerFactory}
     * for the default named persistence unit in
     * {@link DBService#PERSISTENCE_UNIT_NAME} constant.
     *
     * @return The factory that creates EntityManagers
     * configured according to the
     * DBService#PERSISTENCE_UNIT_NAME constant.
     */
    private static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }
}
