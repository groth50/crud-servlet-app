package dao;

import accounts.UserAccount;
import database.ExecuteCallable;
import database.Executor;
import java.util.Collection;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Class that provides data access for users table.
 * Is normally used lambdas from Java 1.8 for callback
 * with {@link Executor} and {@link ExecuteCallable}
 *
 * @autor Alex
 */
public class UserDAOI implements UserDAO {

    /** An object that executes {@link ExecuteCallable} database queries. */
    private final Executor executor;

    /**
     * Constructs a new UserDAOI and initializes
     * an {@link Executor} using {@link EntityManagerFactory}
     *
     * @param entityManagerFactory EntityManagerFactory for
     *                             initializes Executor
     */
    public UserDAOI(EntityManagerFactory entityManagerFactory) {
        this.executor = new Executor(entityManagerFactory);
    }


    /**
     * Return {@link UserAccount} entity by ID from database
     *
     * @param id {@link UserAccount#id} is user ID in application
     *
     * @return UserAccount entity from database
     */
    @Override
    public UserAccount getUserById(long id) {
        return executor.doQuery(entityManager -> entityManager.find(UserAccount.class, id));
    }

    /**
     * Return {@link UserAccount} entity by login from database
     *
     * @param login {@link UserAccount#login}
     *               is user login in application
     *
     * @return {@link UserAccount} entity from database
     */
    @Override
    public UserAccount getUserByLogin(String login) {
        UserAccount userAccount = null;
        userAccount = executor.doQuery(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
            Root<UserAccount> user = criteriaQuery.from(UserAccount.class);
            criteriaQuery.where(criteriaBuilder.equal(user.get("login"), criteriaBuilder.parameter(String.class, "login")));
            TypedQuery<UserAccount> query = entityManager.createQuery(criteriaQuery);
            query.setParameter("login", login);
            return query.getSingleResult();
        });
        return userAccount;
    }

    /**
     * Returns a {@link Collection} contains all
     * {@link UserAccount} entities from database
     *
     * @return Collection contains all UserAccount
     *         entities
     */
    @Override
    public Collection<UserAccount> getAllUsers() {
        Collection<UserAccount> allUsers = null;
        allUsers = executor.doQuery(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
            Root<UserAccount> user = criteriaQuery.from(UserAccount.class);
            TypedQuery<UserAccount> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        });
        return allUsers;
    }

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
     */
    @Override
    public void insertUser(String login, String password, String role) {
        executor.doTransaction(entityManager -> {
            entityManager.persist(new UserAccount(login, password, UserAccount.Role.valueOf(role)));
            return 0;
        });
    }

    /**
     * Delete {@link UserAccount} from database by ID
     *
     * @param id {@link UserAccount#id}
     *           is user ID in application to be removed
     */
    @Override
    public void deleteUser(String id) {
        long longId = Long.parseLong(id);
        executor.doTransaction(entityManager -> {
            UserAccount userAccount = entityManager.find(UserAccount.class, longId);
            entityManager.remove(userAccount);
            return 0;
        });
    }

    /**
     * Save the changed {@link UserAccount} to database in ORM style
     *
     * @param user updateable UserAccount
     */
    @Override
    public void updateUser(UserAccount user) {
        executor.doTransaction(entityManager -> {
            entityManager.merge(user);
            return 0;
        });
    }
}
