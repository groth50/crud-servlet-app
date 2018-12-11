package dao;

import accounts.UserAccount;
import database.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UserDAOHibernate implements UserDAO {
    static final Logger LOGGER = LogManager.getLogger(UserDAOHibernate.class.getName());
    private final Session session;

    public UserDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public UserAccount getUserById(long id) throws DBException { //todo: обернуть в трай кэтч
        return (UserAccount) session.get(UserAccount.class, id);
    }

    @Override
    public UserAccount getUserByLogin(String name) throws DBException {
        Criteria criteria = session.createCriteria(UserAccount.class);
        return (UserAccount) criteria.add(Restrictions.eq("login", name)).uniqueResult();
    }

    @Override
    public Collection<UserAccount> getAllUsers() throws DBException {
//        Criteria criteria = session.createCriteria(UserAccount.class);
//        return (Collection<UserAccount>) criteria.add(Restrictions.eq("login", "*"));
        return new ArrayList<>();
    }

    @Override
    public int insertUser(String name, String password, String role) throws DBException {
        return 0;
    }

    @Override
    public int deleteUser(String id) throws DBException {
        return 0;
    }

    @Override
    public int updateUser(UserAccount user) throws DBException {
        return 0;
    }

    @Override
    public int createTable() throws DBException {
        return 0;
    }

    @Override
    public int dropTable() throws DBException {
        return 0;
    }
}
