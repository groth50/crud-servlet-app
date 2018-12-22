package filter;

import accounts.UserAccount;
import configs.SecurityConfig;
import servlet.GetMainMenuServlet;
import servlet.ConfigServletTest;
import servlet.SignInServlet;
import servlet.SignUpServlet;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SecurityFilterTest extends ConfigServletTest {
    private Filter filter;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        filter = new SecurityFilter();
        Field field = SecurityFilter.class.getDeclaredField("accountService");
        field.setAccessible(true);
        field.set(filter, accountService);
        field.setAccessible(false);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testUnloginedUserSignin() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn(SignInServlet.URL);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(UNREGISTERED_SESSION_ID);
        when(accountService.getUserBySessionId(UNREGISTERED_SESSION_ID)).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void testUnloginedUserSignup() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn(SignUpServlet.URL);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(UNREGISTERED_SESSION_ID);
        when(accountService.getUserBySessionId(UNREGISTERED_SESSION_ID)).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void testLoginedUserSignin() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn(SignInServlet.URL);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_USER);
        when(request.getRequestDispatcher(GetMainMenuServlet.PATH)).thenReturn(dispatcher);

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect(request.getContextPath() + GetMainMenuServlet.URL);
    }

    @Test
    public void testLoginedUserSignup() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn(SignUpServlet.URL);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_USER);
        when(request.getRequestDispatcher(GetMainMenuServlet.PATH)).thenReturn(dispatcher);

        filter.doFilter(request, response, chain);

        verify(response).sendRedirect(request.getContextPath() + GetMainMenuServlet.URL);
    }

    @Test
    public void testUnloginedUserUnpublicPage() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(UNREGISTERED_SESSION_ID);
        when(accountService.getUserBySessionId(UNREGISTERED_SESSION_ID)).thenReturn(null);
        when(request.getRequestDispatcher(SignInServlet.PATH)).thenReturn(dispatcher);

        String url = getUnpublicURL();
        when(request.getServletPath()).thenReturn(url);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testLoginedUserUnpublicPage() throws IOException, ServletException {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_USER);

        String url = getUnpublicURL();
        when(request.getServletPath()).thenReturn(url);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);

    }

    @Test
    public void testUserRoleAccessGranted() throws IOException, ServletException {
        String servletPath = SecurityConfig.getServletPathForRole(UserAccount.Role.USER).get(0);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_USER);
        when(request.getServletPath()).thenReturn(servletPath);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void testAdminRoleAccessGranted() throws IOException, ServletException {
        String servletPath = SecurityConfig.getServletPathForRole(UserAccount.Role.ADMIN).get(0);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_ADMIN);
        when(request.getServletPath()).thenReturn(servletPath);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);

    }

    @Test
    public void testUserRoleAccessDenied() throws IOException, ServletException {
        String servletPath = getDeniedForUserServletPath();
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_USER);
        when(request.getServletPath()).thenReturn(servletPath);
        when(request.getRequestDispatcher("./jsp/_message_box.jsp")).thenReturn(dispatcher);

        filter.doFilter(request, response, chain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(dispatcher).forward(request, response);
    }

    private String getUnpublicURL() {
        return GetMainMenuServlet.URL;
    }

    private String getDeniedForUserServletPath() {
        return "/admininfo";
    }
}