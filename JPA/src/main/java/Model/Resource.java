package Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "resources")
public class Resource {

    public Resource(byte[] file, boolean cypher) {
        this.file = file;
        this.cypher = cypher;
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

    @Column(name = "cypher", nullable = false)
    private boolean cypher;

    @OneToMany(mappedBy = "resource", cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCypher() {
        return cypher;
    }

    public void setCypher(boolean cypher) {
        this.cypher = cypher;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                '}';
    }
}
