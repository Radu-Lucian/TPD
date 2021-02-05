package DAO;

import Model.Role;
import Model.UserRole;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class UserRoleRepository extends GenericRepository<UserRole> {

    private final EntityManagerFactory factory;

    public UserRoleRepository(EntityManagerFactory entityManagerFactory) {
        super(UserRole.class);
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
