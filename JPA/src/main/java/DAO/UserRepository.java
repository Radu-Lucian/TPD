package DAO;

import Model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserRepository extends GenericRepository<User> {

    private final EntityManagerFactory factory;

    public UserRepository(EntityManagerFactory entityManagerFactory) {
        super(User.class);
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

    public User findByUsername(String name) {
        List<User> resultList = findBy("username", name);
        return !resultList.isEmpty() ? resultList.get(0) : null;
    }

    public User findByToken(String token) {
        List<User> resultList = findBy("token", token);
        return !resultList.isEmpty() ? resultList.get(0) : null;
    }

    private List<User> findBy(String entity, String value) {
        EntityManager entityManager = getEntityManager();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);

        Root<User> c = query.from(User.class);
        ParameterExpression<String> paramName = criteriaBuilder.parameter(String.class);
        query.select(c).where(criteriaBuilder.equal(c.get(entity), paramName));
        TypedQuery<User> userTypedQuery = entityManager.createQuery(query);
        userTypedQuery.setParameter(paramName, value);

        return userTypedQuery.getResultList();
    }
}
