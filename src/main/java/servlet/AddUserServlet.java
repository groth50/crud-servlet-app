package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
import database.DBException;
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
 * Servlet which to provide forwarding add user JSP form by GET request,
 * handle and process add user query from POST request.
 *
 * @autor Alex
 */
@WebServlet(name = "AddUser", urlPatterns = "/adduser")
public class AddUserServlet extends HttpServlet {

    /** Standard logger */
    private static final Logger LOGGER = LogManager.getLogger(AddUserServlet.class.getName());

    /**
     * A JSP filename to which that servlet forwards
     * the request to produce it's output
     */
    public static final String PATH = "./jsp/add_user.jsp";

    /** Provides the URL that invokes the servlet */
    public static final String URL = "/adduser";

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
     * Forwarding add user JSP form
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
        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(PATH).forward(request, response);
    }

    /**
     * Handle and process add user query
     *
     * @param request see {@link HttpServletRequest}
     * @param response see {@link HttpServletResponse}
     *
     * @throws ServletException see {@link HttpServlet#doPost(HttpServletRequest, HttpServletResponse)}
     * @throws IOException see {@link HttpServlet#doPost(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());

        PageMessageUtil.clearPageMessageForDoPost(request);
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (login == null || password == null || role == null
                || login.isEmpty() || password.isEmpty() || role.isEmpty()) {
            LOGGER.debug("null form data");
            PageMessageUtil.printBadRequestErrorMessage(request, response,
                    PATH, "Please, insert login and password");
            return;
        }

        UserAccount profile = null;
        try {
            profile = accountService.getUserByLogin(login);
        } catch (DBException e) {
            LOGGER.error(e);
            PageMessageUtil.printServiceUnavailableErrorMessage(request, response, PATH, e.getMessage());
            return;
        }
        if (profile != null) {
            PageMessageUtil.printBadRequestErrorMessage(request, response,
                    PATH, "Login already exist. Please, choose another login.");
            return;
        }
        UserAccount.Role userRole = null;
        try {
            userRole = UserAccount.Role.valueOf(role.toUpperCase());
        }
        finally {
            if (userRole == null) {
                // default role
                userRole = UserAccount.Role.USER;
            }
        }
        try {
            accountService.addNewUser(login, password, userRole);
        } catch (DBException e) {
            LOGGER.error(e.toString());
            PageMessageUtil.printServiceUnavailableErrorMessage(request, response, PATH, e.getMessage());
            return;
        }
        request.getSession().setAttribute("successMessage", "Add user successful!");
        response.sendRedirect(request.getContextPath() + GetAdminMenuServlet.URL);
    }

}
