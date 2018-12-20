package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 *         Прослойка между DAO и самой базой. Управляет циклом жизни Statement из JDBC.
 *         Получает готовый запрос и перенаправляет его в Statement. А так же принимает
 *         функцию для обработки результата.
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
