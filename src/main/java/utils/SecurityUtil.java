package utils;

import accounts.UserAccount;
import configs.SecurityConfig;
import java.util.List;

/**
 * Helper for {@link filter.SecurityFilter}
 *
 * @autor Alex
 */
public class SecurityUtil {

    /**
     * Check access for role, return boolean.
     * True if allowed, false if denied
     *
     * @param servletPath servlet path from URI
     * @param role user role {@link UserAccount.Role}
     *
     * @return true if allowed, false if denied
     */
    public static boolean hasPermission(String servletPath, UserAccount.Role role) {

        List<String> servletPathForRole = SecurityConfig.getServletPathForRole(role);
        return servletPathForRole.contains(servletPath);
    }

    /**
     * Checks if the servlet path
     * is registration or login page
     *
     * @param servletPath servlet path from URI
     *
     * @return true if yes, false if not
     */
    public static boolean isLoginPage(String servletPath) {
        return (servletPath.equals("/signin") || servletPath.equals("/signup"));
    }


}
