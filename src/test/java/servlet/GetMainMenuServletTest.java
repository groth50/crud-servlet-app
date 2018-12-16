package servlet;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetMainMenuServletTest extends ConfigServletTest {
    private GetMainMenuServlet servlet;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        servlet = new GetMainMenuServlet();
        Field field = GetMainMenuServlet.class.getDeclaredField("accountService");
        field.setAccessible(true);
        field.set(servlet, accountService);
        field.setAccessible(false);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        servlet = null;
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher(GetMainMenuServlet.PATH)).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(DEFAULT_SESSION_ID);
        when(accountService.getUserBySessionId(DEFAULT_SESSION_ID)).thenReturn(DEFAULT_USER);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(dispatcher).forward(request, response);
    }
}