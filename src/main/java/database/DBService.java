package database;

import accounts.UserAccount;
import dao.UserDAO;
import dao.UserDAOI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.Collection;



public class DBService {
    static final Logger LOGGER = LogManager.getLogger(DBService.class.getName());
    private static final String PERSISTENCE_UNIT_NAME = "PERSISTENCE";

    private final UserDAO userDAO;

    public DBService() {
        userDAO = new UserDAOI(createEntityManagerFactory());
    }


    public UserAccount getUserById(long id) throws DBException {
        UserAccount userAccount = null;
        try {
            userAccount = userDAO.getUserById(id);
        } catch (NoResultException e) {
            LOGGER.error(e);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException("Sorry, we have trouble with server. Try again.", e);
        }
        return userAccount;
    }

    public UserAccount getUserByLogin(String name) throws DBException {
        UserAccount userAccount = null;
        try {
            userAccount = userDAO.getUserByLogin(name);
        } catch (NoResultException e) {
            LOGGER.error(e);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException("Sorry, we have trouble with server. Try again.", e);
        }
        return userAccount;
    }

    public void addNewUser(String name, String password, UserAccount.Role role) throws DBException {
        try {
            userDAO.insertUser(name, password, role.toString());
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    public void deleteUser(String id) throws DBException {
        try {
            userDAO.deleteUser(id);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    public void updateUser(UserAccount user) throws DBException {
        try {
            userDAO.updateUser(user);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException(e);
        }
    }

    public Collection<UserAccount> getAllUsers() throws DBException {
        Collection<UserAccount> allUsers = null;
        try {
            allUsers = userDAO.getAllUsers();
        } catch (NoResultException e) {
            LOGGER.error(e);
            throw new DBException("Can't find user", e);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw new DBException("Sorry, we have trouble with server. Try again.", e);
        }
        if (allUsers == null || allUsers.isEmpty()) {
            throw new DBException("Can't find user");
        }
        return allUsers;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static EntityManagerFactory createEntityManagerFactory() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        return entityManagerFactory;
    }
}
