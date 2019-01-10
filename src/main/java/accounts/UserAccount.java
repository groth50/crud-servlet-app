package accounts;

import javax.persistence.*;
import java.io.Serializable;

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

    public UserAccount(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }

    public UserAccount(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public UserAccount(long id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = Role.USER;
    }

    public UserAccount(long id, String login, String password, Role role) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }

    public enum Role {
        USER, ADMIN
    }
}
