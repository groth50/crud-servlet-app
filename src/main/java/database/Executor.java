package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * An object that executes {@link ExecuteCallable} database queries. This
 * class provides a way of decoupling queries submission from the
 * mechanics of how each queries will be run, including details of open and close
 * resources, way to database connect, etc. An Executor is normally used lambdas
 * from Java 1.8
 *
 * @autor  Alex
 */
public class Executor {
    static final Logger LOGGER = LogManager.getLogger(Executor.class.getName());
    private final EntityManagerFactory entityManagerFactory;

    public Executor(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public <T> void doTransaction(ExecuteCallable<T> callable) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            transaction = entityManager.getTransaction();
            transaction.begin();
            callable.execute(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            LOGGER.error(e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    //через замыкание
    public <T> T doQuery(ExecuteCallable<T> callable) {
        T result = null;
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            result = callable.execute(entityManager);
        } catch (RuntimeException e) {
            LOGGER.error(e);
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return result;
    }
}
