package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
import database.DBException;
import utils.PageMessageUtil;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet which to provide forwarding main menu JSP form by GET request.
 *
 * @autor  Alex
 */
@WebServlet(name = "GetMainMenu", urlPatterns = "/mainmenu")
public class GetMainMenuServlet extends HttpServlet {

    /** Standard logger */
    private static final Logger LOGGER = LogManager.getLogger(GetMainMenuServlet.class.getName());

    /**
     * A JSP filename to which that servlet forwards
     * the request to produce it's output
     */
    public static final String PATH = "./jsp/main_menu.jsp";

    /** Provides the URL that invokes the servlet */
    public static final String URL = "/mainmenu";

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
     * Forwarding main menu JSP form
     *
     * @param request see {@link HttpServletRequest}
     * @param response see {@link HttpServletResponse}
     *
     * @throws ServletException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     * @throws IOException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doGet from " + this.getClass().getSimpleName());

        PageMessageUtil.clearPageMessageForDoGet(request);
        UserAccount currentUser = accountService.getUserBySessionId(request.getSession().getId());
        Collection<UserAccount> users = null;
        try {
            users = accountService.getAllUsers();
        } catch (DBException e) {
            LOGGER.error(e.toString());
            PageMessageUtil.printServiceUnavailableErrorMessage(request, response, PATH, e.getMessage());
            return;
        }
        if (users == null || users.isEmpty()) {
            PageMessageUtil.printServiceUnavailableErrorMessage(request, response, PATH, "Can't find user");
            return;
        }
        request.setAttribute("currentUser", currentUser);
        request.setAttribute("users", users);

        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(PATH).forward(request, response);
    }
}
