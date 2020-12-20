package DAO;

import Model.Role;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RoleRepository extends GenericRepository<Role>  {

    private final EntityManagerFactory factory;

    public RoleRepository(EntityManagerFactory entityManagerFactory) {
        super(Role.class);
        this.factory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager() {
        try {
            return factory.createEntityManager();
        } catch (Exception ex){
            System.out.println("ERROR");
        }
        return null;
    }
}
