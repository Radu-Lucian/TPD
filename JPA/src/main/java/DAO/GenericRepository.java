package DAO;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public abstract class GenericRepository<T> {

    private final Class<T> entityClass;

    public GenericRepository(Class<T> entity){
        this.entityClass = entity;
    }

    public abstract EntityManager getEntityManager();

    public void create(T entity){
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public void update(T entity) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e){
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public void remove(T entity, int entityId) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.remove((T)entityManager.find(this.entityClass, entityId));
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
    }

    public T find(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            return (T) entityManager.find(this.entityClass, id);
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return null;
    }

    public List<T> findAll() {
        EntityManager entityManager = getEntityManager();
        try {
            CriteriaQuery<Object> criteriaQuery = entityManager.getCriteriaBuilder().createQuery();
            criteriaQuery.select(criteriaQuery.from(this.entityClass));
            List<T> returnedTypes = (List<T>) entityManager.createQuery(criteriaQuery).getResultList();
            return returnedTypes;
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
        } finally {
            entityManager.close();
        }
        return null;
    }
}
