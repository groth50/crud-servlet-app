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
 * Servlet which to provide forwarding update user JSP form by GET request,
 * handle and process update user query from POST request.
 *
 * @autor Alex
 */
@WebServlet(name = "UpdateUser", urlPatterns = "/updateuser")
public class UpdateUserServlet extends HttpServlet {

    /** Standard logger */
    private static final Logger LOGGER = LogManager.getLogger(UpdateUserServlet.class.getName());

    /**
     * A JSP filename to which that servlet forwards
     * the request to produce it's output
     */
    public static final String PATH = "./jsp/update_user.jsp";

    /** Provides the URL that invokes the servlet */
    public static final String URL = "/updateuser";

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
     * Forwarding update user JSP form and set form fields
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
        // set current user for _current_user.jsp
        request.setAttribute("currentUser", currentUser);
        // check id for updatable user from page
        String id = request.getParameter("id");
        int idNum = 0;
        try {
             idNum = Integer.parseInt(id);
        } catch (NumberFormatException ignore) {}
        if (idNum <= 0) {
            PageMessageUtil.printBadRequestErrorMessage(request, response,
                    GetAdminMenuServlet.PATH, "Incorrect id.");
            return;
        }
        UserAccount profile = null;
        try {
            profile = accountService.getUserById(id);
        } catch (DBException e) {
            LOGGER.error(e);
            PageMessageUtil.printServiceUnavailableErrorMessage(request, response, PATH, e.getMessage());
            return;
        }
        if (profile == null) {
            LOGGER.debug("doGet from " + this.getClass().getSimpleName() + " Null pdofile");
            PageMessageUtil.printBadRequestErrorMessage(request, response,
                    GetAdminMenuServlet.PATH, "User not found.");
            return;
        }
        request.getServletContext().setAttribute("user", profile);
        // to automatically select the correct role, in jsp the role of the user is compared with the roles defined here
        request.setAttribute("adminRole", UserAccount.Role.ADMIN);
        request.setAttribute("userRole", UserAccount.Role.USER);
        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(PATH).forward(request, response);
    }

    /**
     * Handle and process update user query
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
        String newLogin = request.getParameter("login");
        String newPassword = request.getParameter("password");
        String roleString = request.getParameter("role");
        UserAccount user = (UserAccount) request.getServletContext().getAttribute("user");
        if (user == null) {
            response.setContentType("text/html;charset=utf-8");
            request.getSession().setAttribute("errorMessage", "Sorry, we didn't find this user.");
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect(request.getContextPath() + GetAdminMenuServlet.URL);
            return;
        }
        if (newLogin == null || newPassword == null || roleString == null || newLogin.isEmpty() || newPassword.isEmpty() || roleString.isEmpty()) {
            //при вводе неправильных данных заполняем поля как было
            request.setAttribute("user", user);
            PageMessageUtil.printBadRequestErrorMessage(request, response,
                    PATH, "Please, insert correct login, role and password.");
            return;
        }
        request.getServletContext().removeAttribute("user");
        // update user
        user.setLogin(newLogin);
        user.setPassword(newPassword);
        UserAccount.Role role = UserAccount.Role.valueOf(roleString.toUpperCase());
        user.setRole(role);
        try {
            accountService.updateUser(user);
        } catch (DBException e) {
            LOGGER.error(e);
            response.setContentType("text/html;charset=utf-8");
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.sendRedirect(request.getContextPath() + GetAdminMenuServlet.URL);
            return;
        }
        response.setContentType("text/html;charset=utf-8");
        request.getSession().setAttribute("successMessage", "Update successful");
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(request.getContextPath() + GetAdminMenuServlet.URL);
    }
}
