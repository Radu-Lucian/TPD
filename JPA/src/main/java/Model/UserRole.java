package Model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "fk_users")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "fk_roles")
    private Role role;

    @Column(name = "downloaded", unique = false, nullable = false)
    private Boolean downloaded;

    public UserRole() {
    }

    public UserRole(User user, Role role) {
        this.id = new UserRoleId(user.getId(), role.getId());
        this.user = user;
        this.role = role;
    }

    public UserRole(User user, Role role, Boolean downloaded) {
        this.id = new UserRoleId(user.getId(), role.getId());
        this.user = user;
        this.role = role;
        this.downloaded = downloaded;
    }

    public UserRoleId getId() {
        return id;
    }

    public void setId(UserRoleId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Boolean downloaded) {
        this.downloaded = downloaded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.id) &&
                Objects.equals(user, userRole.user) &&
                Objects.equals(role, userRole.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, role);
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", user=" + user.getId() +
                ", role=" + role.getId() +
                ", downloaded=" + downloaded +
                '}';
    }
}
