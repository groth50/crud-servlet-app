package dao;

import accounts.UserAccount;
import database.DBException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import database.Executor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionFactoryImpl;


public class UserDAOI implements UserDAO {
    static final Logger LOGGER = LogManager.getLogger(UserDAOI.class.getName());
    private final Executor executor;

    public UserDAOI(SessionFactory sessionFаctory) {
        this.executor = new Executor(sessionFаctory);
        printConnectInfo(sessionFаctory);
    }

    @Override
    public UserAccount getUserById(long id) throws DBException {
        UserAccount userAccount = null;
        try {
            userAccount = (UserAccount) executor.doQuery(session -> {
                return (UserAccount) session.get(UserAccount.class, id);
            });
        } catch (HibernateException e) {
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
            userAccount = (UserAccount) executor.doQuery(session -> {
                Criteria criteria = session.createCriteria(UserAccount.class);
                return (UserAccount) criteria.add(Restrictions.eq("login", name)).uniqueResult();
            });
        } catch (HibernateException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
        return userAccount;
    }

    @Override
    public Collection<UserAccount> getAllUsers() throws DBException {
        Collection<UserAccount> allUsers = null;
        try {
            allUsers = (Collection<UserAccount>) executor.doQuery(session -> {
                Criteria criteria = session.createCriteria(UserAccount.class);
                return (Collection<UserAccount>) criteria.add(Restrictions.isNotNull("login")).list();
            });
        } catch (HibernateException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
        if (allUsers == null || allUsers.isEmpty()) {
            throw new DBException("Can't find user");
        }
        return allUsers;
    }

    @Override
    public long insertUser(String name, String password, String role) throws DBException {
        long id = -1;
        try {
            id = executor.doTransaction(session -> {
                long userId = (Long) session.save(new UserAccount(name, password, UserAccount.Role.valueOf(role)));
                return userId;
            });
        } catch (HibernateException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
        return id;
    }

    @Override
    public void deleteUser(String id) throws DBException {
        long longId = Long.parseLong(id);
        try {
            executor.doTransaction(session -> {
                UserAccount userAccount = (UserAccount) session.get(UserAccount.class, longId);
                session.delete(userAccount);
                return 0;
            });
        } catch (HibernateException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    @Override
    public void updateUser(UserAccount user) throws DBException {
        try {
            executor.doTransaction(session -> {
                session.update(user);
                return 0;
            });
        } catch (HibernateException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

        public void printConnectInfo(SessionFactory sessionFаctory) {
        try {
            SessionFactoryImpl sessionFactoryImpl = (SessionFactoryImpl) sessionFаctory;
            Connection connection = sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            LOGGER.error(e);
        }
    }
}
