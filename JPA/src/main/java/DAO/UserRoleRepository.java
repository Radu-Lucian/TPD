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

    public void create(UserRole entity){
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
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
