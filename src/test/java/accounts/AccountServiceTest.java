package accounts;

import database.DBException;
import org.mockito.Mockito;
import utils.LongId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.when;

public class AccountServiceTest {
    private static final LongId<UserAccount> DEFAULT_ID = new LongId<>(-1);
    private static final LongId<UserAccount> UNREGISTERED_ID = new LongId<>(-2);
    private static final String DEFAULT_LOGIN = "default";
    private static final String UNREGISTERED_LOGIN = "unregistered";
    private static final String DEFAULT_PASSWORD = "123";
    private static final String UNREGISTERED_PASSWORD = "666";
    private static final String DEFAULT_SESSION_ID = "-1";
    private static final String UNREGISTERED_SESSION_ID = "-2";
    private static final UserAccount DEFAULT_USER = getNewUserAccount(DEFAULT_ID, DEFAULT_LOGIN, DEFAULT_PASSWORD);
    private static final UserAccount UNREGISTERED_USER = getNewUserAccount(UNREGISTERED_ID, UNREGISTERED_LOGIN, UNREGISTERED_PASSWORD);

    private AccountService accountService;
    private DBService dbService;

    @Before
    public void setUp() throws Exception {
        dbService = Mockito.mock(DBService.class);
        accountService = new AccountServiceDB(dbService);
    }

    @After
    public void tearDown() throws Exception {
        dbService = null;
    }

    @Test
    public void testGetUserByLogin() throws DBException {
        when(dbService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(DEFAULT_USER);
        UserAccount user = accountService.getUserByLogin(DEFAULT_LOGIN);
        assertNotNull(user);
        assertEquals(DEFAULT_LOGIN, user.getLogin());
    }

    @Test
    public void testAddNewUser() throws DBException {
        when(dbService.getUserByLogin(UNREGISTERED_LOGIN)).thenReturn(UNREGISTERED_USER);
        accountService.addNewUser(UNREGISTERED_LOGIN, UNREGISTERED_PASSWORD);
        UserAccount addedUser = accountService.getUserByLogin(UNREGISTERED_LOGIN);
        assertNotNull(addedUser);
        assertEquals(UNREGISTERED_USER.getLogin(), addedUser.getLogin());
    }

    @Test
    public void testDeleteUser() throws DBException {
        when(dbService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(DEFAULT_USER);
        UserAccount user = accountService.getUserByLogin(DEFAULT_LOGIN);
        assertNotNull(user);
        accountService.deleteUser(DEFAULT_LOGIN);

        when(dbService.getUserByLogin(DEFAULT_LOGIN)).thenReturn(null);
        user = accountService.getUserByLogin(DEFAULT_LOGIN);
        assertNull(user);
    }

    @Test
    public void testGetUserBySessionId() {
        accountService.addSession(UNREGISTERED_SESSION_ID, UNREGISTERED_USER);
        UserAccount user = accountService.getUserBySessionId(UNREGISTERED_SESSION_ID);
        assertNotNull(user);
        assertEquals(user.getLogin(), UNREGISTERED_LOGIN);
    }

    @Test
    public void testAddSession() {
        accountService.addSession(UNREGISTERED_SESSION_ID, UNREGISTERED_USER);
        UserAccount addedUser = accountService.getUserBySessionId(UNREGISTERED_SESSION_ID);
        assertNotNull(addedUser);
        assertEquals(UNREGISTERED_USER.getLogin(), addedUser.getLogin());
    }

    @Test
    public void testDeleteSession() {
        accountService.addSession(UNREGISTERED_SESSION_ID, UNREGISTERED_USER);
        UserAccount user = accountService.getUserBySessionId(UNREGISTERED_SESSION_ID);
        assertNotNull(user);
        accountService.deleteSession(UNREGISTERED_SESSION_ID);
        user = accountService.getUserBySessionId(UNREGISTERED_SESSION_ID);
        assertNull(user);
    }

    private static UserAccount getNewUserAccount(LongId<UserAccount> id, String login, String password) {
        return new UserAccount(id, login, password);
    }
}