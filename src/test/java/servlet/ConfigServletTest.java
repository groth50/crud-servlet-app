package servlet;

import accounts.AccountService;
import accounts.UserAccount;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;

public class ConfigServletTest {
    public static final long DEFAULT_LONG_ID = 1;
    public static final String DEFAULT_STRING_ID = String.valueOf(DEFAULT_LONG_ID);
    public static final String DEFAULT_LOGIN = "Test";
    public static final String UNREGISTERED_LOGIN = "Unregistered";
    public static final String DEFAULT_PASSWORD = "123";
    public static final String UNREGISTERED_PASSWORD = "666";
    public static final String DEFAULT_STRING_ROlE = "USER";
    public static final String DEFAULT_SESSION_ID = "123";
    public static final String UNREGISTERED_SESSION_ID = "-123";
    public static final String CONTEXT_PATH = "";
    public static final UserAccount.Role DEFAULT_ROLE = UserAccount.Role.valueOf(DEFAULT_STRING_ROlE);
    public static final UserAccount DEFAULT_USER = getNewUserAccount(DEFAULT_LONG_ID, DEFAULT_LOGIN, DEFAULT_PASSWORD);
    public static final UserAccount DEFAULT_ADMIN = getNewAdminAccount(DEFAULT_LONG_ID, DEFAULT_LOGIN, DEFAULT_PASSWORD);

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected RequestDispatcher dispatcher;
    protected ServletContext servletContext;
    protected AccountService accountService;
    protected HttpSession session;
    protected FilterChain chain;



    @Before
    public void setUp() throws Exception {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        servletContext = Mockito.mock(ServletContext.class);
        accountService = Mockito.mock(AccountService.class);
        session = Mockito.mock(HttpSession.class);
        chain = Mockito.mock(FilterChain.class);
    }

    @After
    public void tearDown() throws Exception {
        request = null;
        response = null;
        dispatcher = null;
        accountService = null;
        session = null;
        chain = null;
    }

    private static UserAccount getNewUserAccount(long id, String login, String password) {
        return new UserAccount(id, login, password);
    }

    private static UserAccount getNewAdminAccount(long id, String login, String password) {
        return new UserAccount(id, login, password, UserAccount.Role.ADMIN);
    }
}
