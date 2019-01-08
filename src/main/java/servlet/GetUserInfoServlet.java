package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
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
 * Servlet which to provide forwarding user info JSP form by GET request.
 */
@WebServlet(name = "GetUserInfoServlet", urlPatterns = "/userinfo")
public class GetUserInfoServlet extends HttpServlet {

    /** Standard logger */
    private static final Logger LOGGER = LogManager.getLogger(GetUserInfoServlet.class.getName());

    /**
     * A JSP filename to which that servlet forwards
     * the request to produce it's output
     */
    public static final String PATH = "./jsp/user_info.jsp";

    /** Provides the URL that invokes the servlet */
    public static final String URL = "/user_info";

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
     * Forwarding user info JSP form
     *
     * @param request see {@link HttpServletRequest}
     *
     * @param response see {@link HttpServletResponse}
     *
     * @throws ServletException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     *
     * @throws IOException see {@link HttpServlet#doGet(HttpServletRequest, HttpServletResponse)}
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doGet from " + this.getClass().getSimpleName());

        PageMessageUtil.clearPageMessageForDoGet(request);

        UserAccount currentUser = accountService.getUserBySessionId(request.getSession().getId());
        request.setAttribute("currentUser", currentUser);

        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(PATH).forward(request, response);
    }
}
