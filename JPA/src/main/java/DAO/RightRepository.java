package DAO;

import Model.Right;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class RightRepository extends GenericRepository<Right>  {

    private final EntityManagerFactory factory;

    public RightRepository(EntityManagerFactory entityManagerFactory) {
        super(Right.class);
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
