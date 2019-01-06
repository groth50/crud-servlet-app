package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
import database.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageMessageUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "GetMainMenu", urlPatterns = "/mainmenu")
public class GetMainMenuServlet extends HttpServlet {
    private AccountService accountService;
    static final Logger LOGGER = LogManager.getLogger(GetMainMenuServlet.class.getName());
    public static final String PATH = "./jsp/main_menu.jsp";
    public static final String URL = "/mainmenu";

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
