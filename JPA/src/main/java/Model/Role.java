package Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idroles", unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_resource")
    private Resource resource;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "roles_rights",
            joinColumns = @JoinColumn(name = "fk_roles"),
            inverseJoinColumns = @JoinColumn(name = "fk_rights"))
    private List<Right> rights;

    @OneToMany(mappedBy = "role",cascade = CascadeType.MERGE)
    private List<UserRole> users;

    public Role() {
    }

    public Role(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public List<Right> getRights() {
        return rights;
    }

    public void setRights(List<Right> rights) {
        this.rights = rights;
    }

    public List<UserRole> getUsers() {
        return users;
    }

    public void setUsers(List<UserRole> users) {
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", resource=" + resource +
                '}';
    }
}
