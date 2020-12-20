package DAO;

import Model.Resource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ResourceRepository extends GenericRepository<Resource> {

    private final EntityManagerFactory factory;

    public ResourceRepository(EntityManagerFactory entityManagerFactory) {
        super(Resource.class);
        this.factory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager() {
        try {
            return factory.createEntityManager();
        } catch (Exception ex) {
            System.out.println("ERROR");
        }
        return null;
    }
}
