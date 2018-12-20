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

@WebServlet(name = "UpdateUser", urlPatterns = "/updateuser")
public class UpdateUserServlet extends HttpServlet {
    static final Logger LOGGER = LogManager.getLogger(UpdateUserServlet.class.getName());
    public static final String PATH = "./WEB-INF/jsp/update_user.jsp";
    public static final String URL = "/updateuser";

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
        UserAccount currentUser = accountService.getUserBySessionId(request.getSession().getId());
        request.setAttribute("currentUser", currentUser);
        //check param from page
        String id = request.getParameter("id");
        int idNum = 0;
        try {
             idNum = Integer.parseInt(id);
        } catch (NumberFormatException ignore) {}
        if (idNum <= 0) {
            request.setAttribute("errorMessage", "Incorrect id.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(GetAdminMenuServlet.PATH).forward(request, response);
            return;
        }
        //check ID in base
        //todo: убрать пароль из полей
        UserAccount profile = null;
        try {
            profile = accountService.getUserById(id);
        } catch (DBException e) {
            LOGGER.error(e.toString());
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Sorry, we have problems with server. Try again.");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }
        if (profile == null) {
            LOGGER.debug("doGet from " + this.getClass().getSimpleName() + " Null pdofile");
            request.setAttribute("errorMessage", "User not found.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(GetAdminMenuServlet.PATH).forward(request, response);
            return;
        }

        request.getServletContext().setAttribute("user", profile);
        //для автоматического выбора нужной роли, в jsp сравнивается текущая роль с заданными здесь ролями
        request.setAttribute("adminRole", UserAccount.Role.ADMIN);
        request.setAttribute("userRole", UserAccount.Role.USER);
        response.setStatus(HttpServletResponse.SC_OK);
        request.getRequestDispatcher(PATH).forward(request, response);
    }

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
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Please, insert correct login, role and password");

            //при вводе неправильных данных заполняем поля как было
            request.setAttribute("user", user);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }
        request.getServletContext().removeAttribute("user");

        user.setLogin(newLogin);
        user.setPassword(newPassword);
        UserAccount.Role role = UserAccount.Role.valueOf(roleString.toUpperCase());
        user.setRole(role);

        try {
            accountService.updateUser(user);
        } catch (DBException e) {
            LOGGER.error(e);
            response.setContentType("text/html;charset=utf-8");
            request.getSession().setAttribute("errorMessage", "Sorry, we have problems with server."
                    + e.getMessage() + "Try again.");
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
