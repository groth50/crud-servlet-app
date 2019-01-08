package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import utils.PageMessageUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet which to provide logout for users.
 *
 * @autor  Alex
 */
@WebServlet(name = "LogOut", urlPatterns = "/logout")
public class LogOutServlet extends HttpServlet {

    /** Standard logger */
    private static final Logger LOGGER = LogManager.getLogger(LogOutServlet.class.getName());

    /** Provides the URL that invokes the servlet */
    public static final String URL = "/logout";

    /**
     * Managing user accounts in database
     * and their sessions in application.
     */
    private AccountService accountService;

    /**
     * Initialization resources
     *
     * @throws ServletException see {@link HttpServlet#init()}
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.accountService = FactoryAccountService.getAccountService();
    }

    /**
     * Close resources
     */
    @Override
    public void destroy() {
        super.destroy();
        this.accountService = null;
    }

    /**
     * Handle and process logout user request
     *
     * @param request see {@link HttpServletRequest}
     *
     * @param response see {@link HttpServletResponse}
     *
     * @throws IOException see {@link HttpServlet#doPost(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());

        PageMessageUtil.clearPageMessageForDoPost(request);
        String sessionId = request.getSession().getId();
        accountService.deleteSession(sessionId);
        response.setContentType("text/html;charset=utf-8");
        response.sendRedirect(request.getContextPath() + SignInServlet.URL);
    }
}
