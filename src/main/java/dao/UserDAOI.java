package dao;

import accounts.UserAccount;
import database.DBException;

import java.util.Collection;

import database.Executor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
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
    public UserAccount getUserById(long id) throws DBException {
        UserAccount userAccount = null;
        try {
            userAccount = executor.doQuery(entityManager -> entityManager.find(UserAccount.class, id));
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
        if (userAccount == null) {
            throw new DBException("Can't find user");
        }
        return userAccount;
    }

    @Override
    public UserAccount getUserByLogin(String name) throws DBException {
        UserAccount userAccount = null;
        try {
            userAccount = (UserAccount) executor.doQuery(entityManager -> {
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
                Root user = criteriaQuery.from(UserAccount.class);
                criteriaQuery.where(criteriaBuilder.equal(user.get("login"), criteriaBuilder.parameter(String.class, "login")));
                TypedQuery<UserAccount> query = entityManager.createQuery(criteriaQuery);
                query.setParameter("login", name);
                UserAccount account = query.getSingleResult();
                return account;
            });
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
        return userAccount;
    }

    @Override
    public Collection<UserAccount> getAllUsers() throws DBException {
        Collection<UserAccount> allUsers = null;
        try {
            allUsers = executor.doQuery(entityManager -> {
                CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
                CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(UserAccount.class);
                Root user = criteriaQuery.from(UserAccount.class);
                TypedQuery<UserAccount> query = entityManager.createQuery(criteriaQuery);
                Collection<UserAccount> userAccounts = query.getResultList();
                return userAccounts;
            });
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
        if (allUsers == null || allUsers.isEmpty()) {
            throw new DBException("Can't find user");
        }
        return allUsers;
    }

    @Override
    public void insertUser(String name, String password, String role) throws DBException {
        try {
            executor.doTransaction(entityManager -> {
                entityManager.persist(new UserAccount(name, password, UserAccount.Role.valueOf(role)));
                return 0;
            });
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    @Override
    public void deleteUser(String id) throws DBException {
        long longId = Long.parseLong(id);
        try {
            executor.doTransaction(entityManager -> {
                UserAccount userAccount = (UserAccount) entityManager.find(UserAccount.class, longId);
                entityManager.remove(userAccount);
                return 0;
            });
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    @Override
    public void updateUser(UserAccount user) throws DBException {
        try {
            executor.doTransaction(entityManager -> {
                entityManager.merge(user);
                return 0;
            });
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    //todo: print info
//        public void printConnectInfo(SessionFactory sessionFаctory) {
//        try {
//            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFаctory;
//            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
//            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
//            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
//            System.out.println("Driver: " + connection.getMetaData().getDriverName());
//            System.out.println("Autocommit: " + connection.getAutoCommit());
//        } catch (SQLException e) {
//            LOGGER.error(e);
//        }
//    }
}
