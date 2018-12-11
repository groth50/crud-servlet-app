package dao;

import accounts.UserAccount;
import database.Executor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.LongId;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *         Прослойка для общения с конкретной базой - таблицей User. Содержит собственно сами запросы.
 *         Отправляет запрос к базе через Executor и обрабатывает результат посредством лямбда функций.
 *         Реализует команды DQL и DML. Является Object Relation Mapping.
 */
public class UserDAOJDBC implements UserDAO {
    static final Logger LOGGER = LogManager.getLogger(UserDAO.class.getName());
    private Executor executor;

    public UserDAOJDBC(Connection connection) {
        this.executor = new Executor(connection);
    }

    public UserAccount getUserById(long id) throws SQLException {
        String query = "select * from users where id = " + id;
        return executor.execQuery(query, result -> {
            LOGGER.debug(query);
            if (!result.next()) {
                return null;
            }
            return new UserAccount(new LongId<>(result.getLong(1)),
                    result.getString(2), result.getString(3),
                    UserAccount.Role.valueOf(result.getString(4)));
        });
    }

    public UserAccount getUserByLogin(String login) throws SQLException {
        String query = "select * from users where user_name='" + login + "'";
        return executor.execQuery(query, result -> {
            LOGGER.debug(query);
            if (!result.next()) {
                return null;
            }
            long id = result.getLong(1);
            LongId<UserAccount> longId = new LongId<>(id);
            String name = result.getString(2);
            String password = result.getString(3);
            UserAccount.Role role = UserAccount.Role.valueOf(result.getString(4));
            return new UserAccount(longId,
                    name, password,
                    role);
        });
    }

    @Override
    public Collection<UserAccount> getAllUsers() throws SQLException {
        String query = "select * from users";
        Collection<UserAccount> allUsers = executor.execQuery(query, result -> {
            LOGGER.debug(query);
            List<UserAccount> users = new LinkedList<>();
            while (result.next()) {
                long id = result.getLong(1);
                LongId<UserAccount> longId = new LongId<>(id);
                String name = result.getString(2);
                String password = result.getString(3);
                UserAccount.Role role = UserAccount.Role.valueOf(result.getString(4));
                users.add(new UserAccount(longId, name, password, role));
            }
            return users;
        });
        return allUsers;
    }

    public int insertUser(String name, String password, String role) throws SQLException {
        return executor.execUpdate("insert into users (user_name, user_password, user_role) " +
                "values ('" + name + "', '" + password + "', '" + role + "')");
    }

    @Override
    public int deleteUser(String id) throws SQLException {
        return executor.execUpdate("DELETE FROM users WHERE id = " + id);
    }

    @Override
    public int updateUser(UserAccount user) throws SQLException {
        return executor.execUpdate("UPDATE users SET user_name = '" + user.getLogin() + "'," +
                " user_password = '" + user.getPassword() + "', user_role = '" + user.getRole().toString() + "' WHERE id = " + user.getLongId().getId());
    }

    public int createTable() throws SQLException {
        return executor.execUpdate("create table if not exists users (id bigint auto_increment, user_name varchar(256), user_password varchar(256), user_role varchar(6), primary key (id))");
    }

    public int dropTable() throws SQLException {
        return executor.execUpdate("drop table users");
    }
}
