package servlet;

import accounts.AccountService;
import accounts.FactoryAccountService;
import database.DBException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.PageMessageUtil;

/**
 * Servlet which to provide handle and process
 * delete user query from POST request.
 */
@WebServlet(name = "DeleteUser", urlPatterns = "/deleteuser")
public class DeleteUserServlet extends HttpServlet {

    /** Standard logger */
    static final Logger LOGGER = LogManager.getLogger(DeleteUserServlet.class.getName());

    /** Provides the URL that invokes the servlet */
    public static final String URL = "/deleteuser";

    /**
     * Managing user accounts in database
     * and their sessions in application.
     */
    private AccountService accountService;

    /**
     * Initialization resources
     *
     * @throws ServletException
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


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.debug("doPost from " + this.getClass().getSimpleName());
        String id = request.getParameter("userId");
                int idNum = 0;
        try {
            idNum = Integer.parseInt(id);
        } catch (NumberFormatException ignore) {}

        if (idNum <= 0) {
            PageMessageUtil.printBadRequestErrorMessage(request, response, GetAdminMenuServlet.PATH, "Incorrect id.");
            return;
        }

        try {
            accountService.deleteUser(id);
        } catch (DBException e) {
            LOGGER.error(e);
            response.setContentType("text/html;charset=utf-8");
            request.getSession().setAttribute("errorMessage", "Sorry, we have problems with server."
                    + e.getMessage() + "Try again.");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            request.getRequestDispatcher(GetAdminMenuServlet.PATH).forward(request, response);
            return;
        }
        response.setContentType("text/html;charset=utf-8");
        request.getSession().setAttribute("successMessage", "Delete successful");
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(request.getContextPath() + GetAdminMenuServlet.URL);
    }

}
