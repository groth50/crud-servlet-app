package filter;

import accounts.AccountService;
import accounts.FactoryAccountService;
import accounts.UserAccount;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import servlet.GetMainMenuServlet;
import servlet.SignInServlet;
import utils.SecurityUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Check authentication
 * SecurityFilter является Servlet Filter, который выполняет проверки всех запросов к защищенным страницам перед тем,
 * как позволить получить к ним доступ
 */
@WebFilter(filterName = "Security", urlPatterns = {"/signup", "/signin", "/mainmenu", "/adminmenu", "/adduser", "/deleteuser", "/updateuser", "/userinfo"})
public class SecurityFilter implements Filter {
    private AccountService accountService;
    static final Logger LOGGER = LogManager.getLogger(SecurityFilter.class.getName());
    public static final String[] URLS = {"/signup", "/signin", "/mainmenu", "/adminmenu", "/adduser", "/deleteuser", "/updateuser", "/userinfo"};


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.accountService = FactoryAccountService.getAccountService();
    }

    @Override
    public void destroy() {
        this.accountService = null;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("doFilter from " + this.getClass().getSimpleName());
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String servletPath = request.getServletPath();

        UserAccount loginedUser = accountService.getUserBySessionId(request.getSession().getId());
        boolean isLoginPage = SecurityUtil.isLoginPage(servletPath);

        //check log in
        if (loginedUser == null && isLoginPage) {
            chain.doFilter(request, response);
            return;
        } else if (loginedUser != null && isLoginPage) {
            response.setContentType("text/html;charset=utf-8");
            request.getSession().setAttribute("errorMessage", "Your have already sign in.");
            response.sendRedirect(request.getContextPath() + GetMainMenuServlet.URL);
            return;
        } else if (loginedUser == null) {
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Please, sign in or sign up.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.getRequestDispatcher(SignInServlet.PATH).forward(request, response);
            return;
        }

        UserAccount.Role role = loginedUser.getRole();
        if (SecurityUtil.hasPermission(servletPath, role)) {
            chain.doFilter(request, response);
        } else {
            response.setContentType("text/html;charset=utf-8");
            request.setAttribute("errorMessage", "Access denied");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.getRequestDispatcher("./jsp/_message_box.jsp").forward(request, response);
        }
    }
}
