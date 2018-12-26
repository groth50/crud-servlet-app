package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An object that executes {@link ExecuteCallable} database queries. This
 * class provides a way of decoupling queries submission from the
 * mechanics of how each queries will be run, including details of open and close
 * resources, way to database connect, etc. An Executor is normally used lambdas
 * from Java 1.8 for callback.
 *
 * @autor  Alex
 */
public class Executor {

    /** Standard logger */
    private static final Logger LOGGER = LogManager.getLogger(Executor.class.getName());

    /**
     * Object used to interact with the entity manager factory
     * for the persistence unit.
     *
     * @see EntityManagerFactory
     * @see EntityManager
     */
    private final EntityManagerFactory entityManagerFactory;

    public Executor(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Execute a {@link ExecuteCallable} in transaction, use for
     * data manipulation query and another, that need transaction.
     * If transaction fail, call rollback.
     *
     * @param callable - ExecuteCallable represents query
     *
     * @param <T> - this type return {@link ExecuteCallable#execute(EntityManager)}.
     *              Should return int i = 0 in this case and client just ignore this
     */
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

    /**
     * Execute a {@link ExecuteCallable}, use for
     * data query. Don't use transactions.
     *
     * @param callable - ExecuteCallable represents query
     *
     * @param <T> - this type return {@link ExecuteCallable#execute(EntityManager)}.
     *
     * @return T - query result
     */
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
