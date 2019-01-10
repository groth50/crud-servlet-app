package dao;

import accounts.UserAccount;
import java.util.Collection;

/**
 * Interface that provides data access for users table.
 */
public interface UserDAO {

    /**
     * Return {@link UserAccount} entity by ID from the data store
     *
     * @param id {@link UserAccount#id} is user ID in application
     *
     * @return UserAccount entity
     */
    UserAccount getUserById(long id);

    /**
     * Return {@link UserAccount} entity by login from the data store
     *
     * @param login {@link UserAccount#login}
     *              is user login in application
     *
     * @return {@link UserAccount} entity
     */
    UserAccount getUserByLogin(String login);

    /**
     * Returns a {@link Collection} contains all
     * {@link UserAccount} entities from the data store
     *
     * @return Collection contains all UserAccount
     *         entities from the data store
     */
    Collection<UserAccount> getAllUsers();

    /**
     * Add new {@link UserAccount} to data storage
     *
     * @param login {@link UserAccount#login}
     *              is user login in application
     * @param password {@link UserAccount#password}
     *                 is user password in application
     *
     * @param role {@link UserAccount#role}
     *             is user role in application
     */
    void insertUser(String login, String password, String role);

    /**
     * Delete {@link UserAccount} from data storage by ID
     *
     * @param id {@link UserAccount#id}
     *           is user ID in application to be removed
     */
    void deleteUser(String id);

    /**
     * Save the changed {@link UserAccount} to data storage in ORM style
     *
     * @param user updateable UserAccount
     */
    void updateUser(UserAccount user);
}