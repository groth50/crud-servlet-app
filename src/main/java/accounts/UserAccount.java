package accounts;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Class represents user account in application.
 * This is entity instances for Java Persistence.
 *
 * @autor Alex
 */
@Entity
@Table(name = "users")
public class UserAccount implements Serializable {
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_name", unique = true)
    private String login;
    @Column(name = "user_password")
    private String password; //todo: убрать пасс из базы и перейти на хэш
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    // Constructor for JPA provider
    public UserAccount() {
    }

    /**
     * Constructs new UserAccount entity
     * with default role {@link Role#USER}.
     *
     * @param login - user login
     * @param password - user password
     */
    public UserAccount(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }

    /**
     * Constructs new UserAccount entity.
     *
     * @param login - user login
     * @param password - user password
     * @param role - user {@link Role}
     */
    public UserAccount(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    // Constructor for tests
    public UserAccount(long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }

    // Constructor for tests
    public UserAccount(long id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    /**
     * Getter for {@link UserAccount#id}
     *
     * @return user ID - unique integer, foreign key
     */
    public long getId() {
        return id;
    }

    /**
     * Getter for {@link UserAccount#login}
     *
     * @return {@link String} user login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Setter for {@link UserAccount#login}
     *
     * @param login - {@link String} user login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Getter for {@link UserAccount#password}
     *
     * @return {@link String} user password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter for {@link UserAccount#password}
     *
     * @param password - {@link String} user password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter for {@link UserAccount#role}
     *
     * @return user {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setter for {@link UserAccount#role}
     *
     * @param role - user {@link Role}
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns a string representation of the UserAccount
     *
     * @return {@link String} with UserAccount
     *          ID, login, password and role
     */
    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    /**
     * Represents user role in application
     * with different access
     *
     * USER - can login and see other users
     * ADMIN - user access and can create, update,
     *         delete another {@link UserAccount}
     */
    public enum Role {
        USER, ADMIN
    }
}
