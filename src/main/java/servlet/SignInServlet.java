package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
import database.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageMessageUtil;

@WebServlet(name = "SignIn", urlPatterns = "/signin")
public class SignInServlet extends HttpServlet {
    private AccountService accountService;
    static final Logger LOGGER = LogManager.getLogger(SignInServlet.class.getName());
    public static final String PATH = "./WEB-INF/jsp/sign_in.jsp";
    public static final String URL = "/signin";

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());
        PageMessageUtil.clearPageMessageForDoPost(request);


        String login = request.getParameter("login");
        String password = request.getParameter("password");


        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Please, insert your login and password");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }

        UserAccount profile = null;
        try {
            profile = accountService.getUserByLogin(login);
        } catch (DBException e) {
            LOGGER.error(e.toString());
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", e.getMessage());
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }
        if (profile == null || !profile.getPassword().equals(password)) {
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Invalid login or password");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }
        String sessionId = request.getSession().getId();
        accountService.addSession(sessionId, profile);
        request.getSession().setAttribute("successMessage", "Your login is " + login + " and session ID is " + sessionId);
        response.sendRedirect(request.getContextPath() + GetMainMenuServlet.URL);
    }
}
