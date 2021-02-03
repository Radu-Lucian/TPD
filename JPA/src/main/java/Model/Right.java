package Model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "rights")
public class Right {

    public Right() {

    }

    @Id
    @Column(name = "idrights", nullable = false, unique = true)
    private int id;

    @Column(name = "type", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RightType type;

    @ManyToMany(mappedBy = "rights", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles;

    public RightType getType() {
        return type;
    }

    public void setType(RightType type) {
        this.type = type;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Right{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
