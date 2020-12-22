package Model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "resources")
public class Resource {

    public Resource(byte[] file) {
        this.file = file;
    }

    public Resource() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idresources", unique = true, nullable = false)
    private int id;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Role> roles;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", roles=" + roles +
                '}';
    }
}