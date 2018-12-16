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

import java.util.Collection;


public class DBService {
    static final Logger LOGGER = LogManager.getLogger(DBService.class.getName());
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "validate";

    private final UserDAO userDAO;

    public DBService() {
        Configuration configuration = getMySqlConfiguration();
        SessionFactory sessionFactory = createSessionFactory(configuration);
        userDAO = new UserDAOI(sessionFactory);
    }

    private Configuration getMySqlConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserAccount.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/db_example");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "1234");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }

    public static void main(String[] args) throws DBException {
        DBService service = new DBService();

        System.out.println(service.getUserById(1));
        System.out.println(service.getUserByLogin("admin"));

        long id = service.addNewUser("lol", "1234", UserAccount.Role.USER);
        System.out.println(id);
        System.out.println(service.getUserById(id));

        //System.out.println(service.getUserById(500));
        //System.out.println(service.getUserByLogin("ololosh"));
        //System.out.println(service.addNewUser("admin", "100500", UserAccount.Role.USER));
    }

    public UserAccount getUserById(long id) throws DBException {
        return userDAO.getUserById(id);
    }

    public UserAccount getUserByLogin(String name) throws DBException {
        return userDAO.getUserByLogin(name);
    }

    public long addNewUser(String name, String password, UserAccount.Role role) throws DBException {
        long id = userDAO.insertUser(name, password, role.toString());
        if (id <= 0) {
            throw new DBException("Can't insert user.");
        }
        return id;
    }

    public void deleteUser(String id) throws DBException {
        userDAO.deleteUser(id);
    }

    public void updateUser(UserAccount user) throws DBException {
        userDAO.updateUser(user);
    }

    public Collection<UserAccount> getAllUsers() throws DBException {
        return userDAO.getAllUsers();
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
