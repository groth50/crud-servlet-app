package dao;

import accounts.UserAccount;
import database.Executor;
import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class UserDAOI implements UserDAO {
    static final Logger LOGGER = LogManager.getLogger(UserDAOI.class.getName());
    private final Executor executor;

    public UserDAOI(EntityManagerFactory entityManagerFactory) {
        this.executor = new Executor(entityManagerFactory);
    }

    @Override
    public UserAccount getUserById(long id) {
        UserAccount userAccount = executor.doQuery(entityManager -> entityManager.find(UserAccount.class, id));
        return userAccount;
    }

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
            UserAccount account = query.getSingleResult();
            return account;
        });
        return userAccount;
    }

    @Override
    public Collection<UserAccount> getAllUsers() {
        Collection<UserAccount> allUsers = null;
        allUsers = executor.doQuery(entityManager -> {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
            Root<UserAccount> user = criteriaQuery.from(UserAccount.class);
            TypedQuery<UserAccount> query = entityManager.createQuery(criteriaQuery);
            Collection<UserAccount> userAccounts = query.getResultList();
            return userAccounts;
        });
        return allUsers;
    }

    @Override
    public void insertUser(String name, String password, String role) {
        executor.doTransaction(entityManager -> {
            entityManager.persist(new UserAccount(name, password, UserAccount.Role.valueOf(role)));
            return 0;
        });
    }

    @Override
    public void deleteUser(String id) {
        long longId = Long.parseLong(id);
        executor.doTransaction(entityManager -> {
            UserAccount userAccount = entityManager.find(UserAccount.class, longId);
            entityManager.remove(userAccount);
            return 0;
        });
    }

    @Override
    public void updateUser(UserAccount user) {
        executor.doTransaction(entityManager -> {
            entityManager.merge(user);
            return 0;
        });
    }
}
