package accounts;

import database.DBException;
import database.DBService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для управления аккаунтами и их сессиями.
 */
public class AccountServiceDB implements AccountService {
    private final DBService dbService;
    private final Map<String, UserAccount> sessionIdToProfile; //todo: слабые ссылки и многопоточность

    public AccountServiceDB() {
        dbService = new DBService();
        sessionIdToProfile = new HashMap<>();
    }

    public AccountServiceDB(DBService dbService) {
        this.dbService = dbService;
        sessionIdToProfile = new HashMap<>();
    }



    @Override
    public void addNewUser(String login, String password) throws DBException {
        UserAccount.Role roleDefault = UserAccount.Role.USER;
        dbService.addNewUser(login, password, roleDefault);
    }

    @Override
    public void addNewUser(String login, String password, UserAccount.Role role) throws DBException {
        dbService.addNewUser(login, password, role);
    }

    @Override
    public void deleteUser(String id) throws DBException {
        dbService.deleteUser(id);
    }

    @Override
    public void updateUser(UserAccount user) throws DBException {
        dbService.updateUser(user);
    }

    @Override
    public UserAccount getUserByLogin(String login) throws DBException {
        return dbService.getUserByLogin(login);
    }

    @Override
    public UserAccount getUserById(String id) throws DBException {
        return dbService.getUserById(Long.parseLong(id));
    }

    @Override
    public UserAccount getUserBySessionId(String sessionId) {
        return sessionIdToProfile.get(sessionId);
    }

    @Override
    public void addSession(String sessionId, UserAccount userAccount) {
        sessionIdToProfile.put(sessionId, userAccount);
    }

    @Override
    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }

    @Override
    public Collection<UserAccount> getAllUsers() throws DBException {
        return dbService.getAllUsers();
    }

}
