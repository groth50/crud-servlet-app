package configs;

import accounts.UserAccount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Configuration class for roles
 * and their access rights
 *
 * @author Alex
 * @see    filter.SecurityFilter
 */
public class SecurityConfig {

    /**
     * Map {@link UserAccount.Role} on
     * {@link List} of available servlet path
     */
    private static final Map<UserAccount.Role, List<String>> mapConfig = new HashMap<>();

    static {
        init();
    }

    /**
     * Init map {@link UserAccount.Role} on
     * {@link List} of available servlet path
     */
    private static void init() {
        // List for "USER" role
        List<String> urlPatternsUser = new ArrayList<String>();

        urlPatternsUser.add("/mainmenu");
        urlPatternsUser.add("/userinfo");

        mapConfig.put(UserAccount.Role.USER, urlPatternsUser);

        // List for "ADMIN" role
        List<String> urlPatternsAdmin = new ArrayList<String>();

        urlPatternsAdmin.add("/adminmenu");
        urlPatternsAdmin.add("/mainmenu");
        urlPatternsAdmin.add("/adduser");
        urlPatternsAdmin.add("/deleteuser");
        urlPatternsAdmin.add("/updateuser");
        urlPatternsAdmin.add("/userinfo");

        mapConfig.put(UserAccount.Role.ADMIN, urlPatternsAdmin);
    }

    /**
     * Return {@link Set} of all roles
     *
     * @return Set of all roles
     */
    public static Set<UserAccount.Role> getAllAppRoles() {
        return mapConfig.keySet();
    }

    /**
     * Return {@link List} of available
     * servlet path for {@link UserAccount.Role}
     *
     * @param role user role
     *
     * @return List of available servlet path
     */
    public static List<String> getServletPathForRole(UserAccount.Role role) {
        return mapConfig.get(role);
    }

}
