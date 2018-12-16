package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *         Прослойка между DAO и самой базой. Управляет циклом жизни Statement из JDBC.
 *         Получает готовый запрос и перенаправляет его в Statement. А так же принимает
 *         функцию для обработки результата.
 */
public class Executor {
    static final Logger LOGGER = LogManager.getLogger(Executor.class.getName());
    private final SessionFactory sessionFactory;

    public Executor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <T> T doTransaction(ExecuteCallable<T> callable) {
        T result = null;
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            result = callable.execute(session);
            transaction.commit();
        } catch (HibernateException e) {
            LOGGER.error(e);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

//    public <T, S> T doQuery(Class<T> clazz, S param, ExecuteCallable<T> callable) throws HibernateException {
//        T result = null;
//        Session session = null;
//        try {
//            session = sessionFactory.openSession();
//            result = callable.execute(session);
//        } catch (HibernateException e) {
//            LOGGER.error(e);
//            throw e;
//        } finally {
//            if (session != null) {
//                session.close();
//            }
//        }
//        return result;
//    }
    //через замыкание
    public <T> T doQuery(ExecuteCallable<T> callable) throws HibernateException {
        T result = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            result = callable.execute(session);
        } catch (HibernateException e) {
            LOGGER.error(e);
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }
}
