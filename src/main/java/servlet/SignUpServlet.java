package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
import database.DBException;
import utils.PageMessageUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet for create new account.
 * @autor Alex
 */
@WebServlet(name = "SignUp", urlPatterns = "/signup")
public class SignUpServlet extends HttpServlet {
    static final Logger LOGGER = LogManager.getLogger(SignUpServlet.class.getName());

    /** Path for SignUp page */
    public static final String PATH = "./WEB-INF/jsp/sign_up.jsp";

    /** Servlet path */
    public static final String URL = "/signup";

    /** Managing user accounts and their sessions */
    private AccountService accountService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.accountService = FactoryAccountService.getAccountService();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.accountService = null;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doGet from " + this.getClass().getSimpleName());
        PageMessageUtil.clearPageMessageForDoGet(request);
        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(PATH).forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());
        PageMessageUtil.clearPageMessageForDoPost(request);
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            LOGGER.debug("null login");
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Please, insert your login and password");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        UserAccount profile = null;
        //todo: вынести в отдельный метод?
        try {
            profile = accountService.getUserByLogin(login);
        } catch (DBException e) {
            LOGGER.error(e.toString());
            response.setContentType("text/html;charset=utf-8"); //todo: сделать фильтр на кодировку
            request.setAttribute("errorMessage", e.getMessage());
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        if (profile == null) {
            LOGGER.debug("null profile");
            try {
                accountService.addNewUser(login, password);
            } catch (DBException e) {
                LOGGER.error(e.toString());
                response.setContentType("text/html;charset=utf-8");
                request.setAttribute("errorMessage", "Sorry, we have problems with server. Try again.");
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                request.getRequestDispatcher(PATH).forward(request, response);
                return;
            }
            request.getSession().setAttribute("successMessage", "Sign up successful! Please, sign in.");
            response.sendRedirect(request.getContextPath() + SignInServlet.URL);
            return;
        }

        LOGGER.debug("profile already exist");
        response.setContentType("text/html;charset=utf-8");
        request.setAttribute("errorMessage", "Login already exist. Please, choose another login.");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        request.getRequestDispatcher(PATH).forward(request, response);
    }
}